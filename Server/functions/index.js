const functions = require('firebase-functions');
const admin = require('firebase-admin');
const rp = require('request-promise');
const cheerio = require('cheerio');
const isNumber = require('is-number');
const URL = require('url');

// Setup admin SDK
admin.initializeApp();

exports.updateMenu = functions.https.onRequest((request, response) => {
  // Read all canteens
  admin.firestore().collection("canteens").get().then((documents) => {
    var promises = new Array();
    documents.forEach((canteenDoc) => {
      var canteen = canteenDoc.data()
      promises.push(parseMenu(canteen.id, canteen.menuUrl))
    });

    // When all canteens are loaded
    return Promise.all(promises).then((results) => {
      console.log("Update completed")
      return response.status(200).send({})
    }).catch((e) => {
      console.error(e)
      response.status(500).send({})
    });
  }).catch((e) => {
    console.error(e)
    response.status(500).send({})
  });
});

function parseMenu(canteenId, url) {
  return new Promise((resolve, reject) => {
      parseWeek(canteenId, url, new Array(), resolve, reject)
  });
}

function parseWeek(canteenId, url, menu, resolve, reject) {
  console.log("Accessing " +  url)
  rp(url).then((htmlString) => {
      console.log("Parsing content from " + url)
      var $ = cheerio.load(htmlString);

      var thisMenu = [
        parseDay($, '#tab-mon'),
        parseDay($, '#tab-tue'),
        parseDay($, '#tab-wed'),
        parseDay($, '#tab-thu'),
        parseDay($, '#tab-fri'),
        parseDay($, '#tab-sat')
      ].filter(i => i.dishes.length > 0)
      console.log("Found " + thisMenu.length + " dishes in " + url)
      menu = menu.concat(thisMenu);

      var linkToNextWeek = $('.next-week');
      if (linkToNextWeek) {
        var linkHref =  $(linkToNextWeek).attr('href');
        if (linkHref) {
          var parsedUrl = URL.parse(url)
          console.log("Following link to next week from in " + url + "...")
          return parseWeek(canteenId, parsedUrl.protocol + '//' + parsedUrl.host + linkHref, menu, resolve, reject);
        } else {
          console.log("Empty href in " + url + ", menu complete")
          return storeMenu(canteenId, menu, resolve, reject)
        }
      } else {
        console.log("No link to next week in " + url + ", menu complete")
        return storeMenu(canteenId, menu, resolve, reject)
      }
    })
    .catch((e) => {
      console.error("Unable to load " + url)
      reject(e);
    });
}

function parseDay($, tabName) {
  var date = formatDate(parseDate($('h3', tabName).text()))
  var dishes = new Array();

  var additivesTable = {};
  $('h4').filter((i, h4) => { return $(h4).text() === 'Kennzeichnungen/Zusatzstoffe' }).next('.menu-zusatzstoffe').children('dt').each((i, h4) => {
    additivesTable[$(h4).text()] = $(h4).next('dd').text() || ""
  })
  var alergensTable = {};
  $('h4').filter((i, h4) => { return $(h4).text() === 'Allergene' }).next('.menu-zusatzstoffe').children('dt').each((i, h4) => {
    alergensTable[$(h4).text()] = $(h4).next('dd').text() || ""
  })

  $('.row', tabName).each((i, dish) => {
    // Remove additives and allergens, they will show up in the description, we don't want them there
    // Additives are numbers, allergens are letters
    var contents = (improveString($(dish).find('.zusatzsstoffe.show-with-allergenes').text()) || "").split(',').filter(i => i && i.length > 0);
    var msc = contents.filter(i => i === 'MSC').length > 0;
    var additives = contents.filter(i => isNumber(i)).map((i) => { return { key: i, description: additivesTable[i] } });
    var alergens = contents.filter(i => !isNumber(i) && i !== 'MSC').map((i) => { return { key: i, description: alergensTable[i] } });
    $(dish).find('.zusatzsstoffe').remove()

    dishes.push({
      date: date,
      title: improveString($(dish).find('h4').text()),
      description: improveString($(dish).find('.menu-info').html()),
      additives: additives,
      alergens: alergens,
      vegetarian: $(dish).hasClass('vegetarisch'),
      vegan: $(dish).hasClass('vegan'),
      veganOption: $(dish).hasClass('wunsch-vegan'),
      msc: msc,
      priceStudents: improveString($(dish).find('.price-studierende').next().text()) || "",
      priceEmployees: improveString($(dish).find('.price-mitarbeiter').next().text()) || "",
      priceGuests: improveString($(dish).find('.price-gaeste').next().text()) || ""
    });
  });

  return {
    date: date,
    dishes: dishes.filter(dish => dish.title.indexOf("keine Essensausgabe") < 0)
  };
}

function parseDate(date) {
  var now = new Date()
  var cleanDate = date.split(" ")[1]
  var day = Number.parseInt(cleanDate.split(".")[0])
  var month =  Number.parseInt(cleanDate.split(".")[1])
  var currentYear = now.getFullYear()
  var currentMonth = now.getMonth()
  var year = month === 1 && currentMonth === 12 ? currentYear + 1 : currentYear
  return new Date(year, month - 1, day + 1)
}

function improveString(string) {
  if (string) {
    return string.trim()
      .replace(/  +/g, ' ')
      .replace('enthält Allergene: ', ',')
      .replace('Kennzeichnungen/Zusatzstoffe: ', '')
  }
  return string
}

function storeMenu(canteenId, menu, resolve, reject) {
  menu.forEach((day) => {
    console.log("Storing dishes for canteen", canteenId, "at day", day.date);
    day.dishes.forEach((dish, i) => {
      admin.firestore().collection("canteens").doc(String(canteenId)).collection("menu").doc(day.date).collection('dishes').doc(String(i)).set(dish).then(() => {
        return resolve();
      }).catch((e) => {
        reject(e);
      })
    });
  })
}

function formatDate(date) {
    var d = new Date(date),
        month = String(d.getMonth() + 1),
        day = String(d.getDate()),
        year = d.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [year, month, day].join('-');
}
