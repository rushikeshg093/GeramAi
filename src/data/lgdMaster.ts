import { District, Taluka, GramPanchayat, Village } from '../types';

// =========================================================================
// OFFICIAL MAHARASHTRA LGD DATASET (Local Government Directory - Govt of India)
// Source: Government of India Local Government Directory (LGD)
// Total Districts: 36
// Total Talukas / Subdistricts: 358
// Total Gram Panchayats (PRI Local Bodies): 28034
// Total Mapped Villages: 43124
// Total GP-to-Village Mappings: 43352
// =========================================================================

export const lgdSummary = {
  "totalDistricts": 36,
  "totalTalukas": 358,
  "totalGramPanchayats": 28034,
  "totalVillages": 43124,
  "totalMappings": 43352
};

// 1. ALL 36 OFFICIAL MAHARASHTRA DISTRICTS (LGD CODES)
export const districts: District[] = [
  {
    "id": "466",
    "code": "466",
    "nameEn": "Ahilyanagar",
    "nameMr": "अहिल्यानगर"
  },
  {
    "id": "467",
    "code": "467",
    "nameEn": "Akola",
    "nameMr": "अकोला"
  },
  {
    "id": "468",
    "code": "468",
    "nameEn": "Amravati",
    "nameMr": "अमरावती"
  },
  {
    "id": "470",
    "code": "470",
    "nameEn": "Beed",
    "nameMr": "बीड"
  },
  {
    "id": "471",
    "code": "471",
    "nameEn": "Bhandara",
    "nameMr": "भंडारा"
  },
  {
    "id": "472",
    "code": "472",
    "nameEn": "Buldhana",
    "nameMr": "बुलढाणा"
  },
  {
    "id": "473",
    "code": "473",
    "nameEn": "Chandrapur",
    "nameMr": "चंद्रपूर"
  },
  {
    "id": "469",
    "code": "469",
    "nameEn": "Chhatrapati Sambhajinagar",
    "nameMr": "छत्रपती संभाजीनगर"
  },
  {
    "id": "488",
    "code": "488",
    "nameEn": "Dharashiv",
    "nameMr": "धाराशिव"
  },
  {
    "id": "474",
    "code": "474",
    "nameEn": "Dhule",
    "nameMr": "धुळे"
  },
  {
    "id": "475",
    "code": "475",
    "nameEn": "Gadchiroli",
    "nameMr": "गडचिरोली"
  },
  {
    "id": "476",
    "code": "476",
    "nameEn": "Gondia",
    "nameMr": "गोंदीया"
  },
  {
    "id": "477",
    "code": "477",
    "nameEn": "Hingoli",
    "nameMr": "हिंगोली"
  },
  {
    "id": "478",
    "code": "478",
    "nameEn": "Jalgaon",
    "nameMr": "जळगाव"
  },
  {
    "id": "479",
    "code": "479",
    "nameEn": "Jalna",
    "nameMr": "जालना"
  },
  {
    "id": "480",
    "code": "480",
    "nameEn": "Kolhapur",
    "nameMr": "कोल्हापूर"
  },
  {
    "id": "481",
    "code": "481",
    "nameEn": "Latur",
    "nameMr": "लातूर"
  },
  {
    "id": "482",
    "code": "482",
    "nameEn": "Mumbai",
    "nameMr": "मुंबई"
  },
  {
    "id": "483",
    "code": "483",
    "nameEn": "Mumbai Suburban",
    "nameMr": "मुंबई उपनगर"
  },
  {
    "id": "484",
    "code": "484",
    "nameEn": "Nagpur",
    "nameMr": "नागपूर"
  },
  {
    "id": "485",
    "code": "485",
    "nameEn": "Nanded",
    "nameMr": "नांदेड"
  },
  {
    "id": "486",
    "code": "486",
    "nameEn": "Nandurbar",
    "nameMr": "नंदूरबार"
  },
  {
    "id": "487",
    "code": "487",
    "nameEn": "Nashik",
    "nameMr": "नाशिक"
  },
  {
    "id": "665",
    "code": "665",
    "nameEn": "Palghar",
    "nameMr": "पालघर"
  },
  {
    "id": "489",
    "code": "489",
    "nameEn": "Parbhani",
    "nameMr": "परभणी"
  },
  {
    "id": "490",
    "code": "490",
    "nameEn": "Pune",
    "nameMr": "पुणे"
  },
  {
    "id": "491",
    "code": "491",
    "nameEn": "Raigad",
    "nameMr": "रायगड"
  },
  {
    "id": "492",
    "code": "492",
    "nameEn": "Ratnagiri",
    "nameMr": "रत्नागिरी"
  },
  {
    "id": "493",
    "code": "493",
    "nameEn": "Sangli",
    "nameMr": "सांगली"
  },
  {
    "id": "494",
    "code": "494",
    "nameEn": "Satara",
    "nameMr": "सातारा"
  },
  {
    "id": "495",
    "code": "495",
    "nameEn": "Sindhudurg",
    "nameMr": "सिंधुदुर्ग"
  },
  {
    "id": "496",
    "code": "496",
    "nameEn": "Solapur",
    "nameMr": "सोलापूर"
  },
  {
    "id": "497",
    "code": "497",
    "nameEn": "Thane",
    "nameMr": "ठाणे"
  },
  {
    "id": "498",
    "code": "498",
    "nameEn": "Wardha",
    "nameMr": "वर्धा"
  },
  {
    "id": "499",
    "code": "499",
    "nameEn": "Washim",
    "nameMr": "वाशिम"
  },
  {
    "id": "500",
    "code": "500",
    "nameEn": "Yavatmal",
    "nameMr": "यवतमाळ"
  }
];

// 2. ALL 358 OFFICIAL MAHARASHTRA SUB-DISTRICTS / TALUKAS (LGD CODES)
export const talukas: Taluka[] = [
  {
    "id": "4004",
    "code": "4004",
    "districtId": "468",
    "nameEn": "Achalpur",
    "nameMr": "अचलपूर"
  },
  {
    "id": "4062",
    "code": "4062",
    "districtId": "475",
    "nameEn": "Aheri",
    "nameMr": "अहेरी"
  },
  {
    "id": "4228",
    "code": "4228",
    "districtId": "481",
    "nameEn": "Ahmadpur",
    "nameMr": "अहमदपूर"
  },
  {
    "id": "4292",
    "code": "4292",
    "districtId": "480",
    "nameEn": "Ajra",
    "nameMr": "आजरा"
  },
  {
    "id": "4254",
    "code": "4254",
    "districtId": "496",
    "nameEn": "Akkalkot",
    "nameMr": "अक्कलकोट"
  },
  {
    "id": "3950",
    "code": "3950",
    "districtId": "486",
    "nameEn": "Akkalkuwa",
    "nameMr": "अक्कलकुवा"
  },
  {
    "id": "3991",
    "code": "3991",
    "districtId": "467",
    "nameEn": "Akola",
    "nameMr": "अकोला"
  },
  {
    "id": "4201",
    "code": "4201",
    "districtId": "466",
    "nameEn": "Akole",
    "nameMr": "अकोले"
  },
  {
    "id": "3989",
    "code": "3989",
    "districtId": "467",
    "nameEn": "Akot",
    "nameMr": "अकोट"
  },
  {
    "id": "3951",
    "code": "3951",
    "districtId": "486",
    "nameEn": "Akrani",
    "nameMr": "अकर्नी"
  },
  {
    "id": "4177",
    "code": "4177",
    "districtId": "491",
    "nameEn": "Alibag",
    "nameMr": "अलिबाग"
  },
  {
    "id": "3969",
    "code": "3969",
    "districtId": "478",
    "nameEn": "Amalner",
    "nameMr": "अमळनेर"
  },
  {
    "id": "4129",
    "code": "4129",
    "districtId": "479",
    "nameEn": "Ambad",
    "nameMr": "अंबड"
  },
  {
    "id": "4170",
    "code": "4170",
    "districtId": "497",
    "nameEn": "Ambarnath",
    "nameMr": "अंबरनाथ"
  },
  {
    "id": "4188",
    "code": "4188",
    "districtId": "490",
    "nameEn": "Ambegaon",
    "nameMr": "आंबेगाव"
  },
  {
    "id": "4225",
    "code": "4225",
    "districtId": "470",
    "nameEn": "Ambejogai",
    "nameMr": "आंबेजोगाई"
  },
  {
    "id": "4047",
    "code": "4047",
    "districtId": "476",
    "nameEn": "Amgaon",
    "nameMr": "आमगाव"
  },
  {
    "id": "4009",
    "code": "4009",
    "districtId": "468",
    "nameEn": "Amravati",
    "nameMr": "अमरावती"
  },
  {
    "id": "7179",
    "code": "7179",
    "districtId": "483",
    "nameEn": "Andheri",
    "nameMr": "अंधेरी"
  },
  {
    "id": "4003",
    "code": "4003",
    "districtId": "468",
    "nameEn": "Anjangaon Surji",
    "nameMr": "अंजनगाव सुर्जी"
  },
  {
    "id": "4099",
    "code": "4099",
    "districtId": "485",
    "nameEn": "Ardhapur",
    "nameMr": "अर्धापूर"
  },
  {
    "id": "4050",
    "code": "4050",
    "districtId": "476",
    "nameEn": "Arjuni Morgaon",
    "nameMr": "अर्जुनी मोरगाव"
  },
  {
    "id": "4053",
    "code": "4053",
    "districtId": "475",
    "nameEn": "Armori",
    "nameMr": "आरमोरी"
  },
  {
    "id": "4088",
    "code": "4088",
    "districtId": "500",
    "nameEn": "Arni",
    "nameMr": "अर्नी"
  },
  {
    "id": "4017",
    "code": "4017",
    "districtId": "498",
    "nameEn": "Arvi",
    "nameMr": "आर्वी"
  },
  {
    "id": "4015",
    "code": "4015",
    "districtId": "498",
    "nameEn": "Ashti",
    "nameMr": "आष्टी"
  },
  {
    "id": "4215",
    "code": "4215",
    "districtId": "470",
    "nameEn": "Ashti",
    "nameMr": "आष्टी"
  },
  {
    "id": "4300",
    "code": "4300",
    "districtId": "493",
    "nameEn": "Atpadi",
    "nameMr": "आटपाडी"
  },
  {
    "id": "4113",
    "code": "4113",
    "districtId": "477",
    "nameEn": "Aundha (Nagnath)",
    "nameMr": "औंढा (नागनाथ)"
  },
  {
    "id": "4232",
    "code": "4232",
    "districtId": "481",
    "nameEn": "Ausa",
    "nameMr": "औसा"
  },
  {
    "id": "4080",
    "code": "4080",
    "districtId": "500",
    "nameEn": "Babulgaon",
    "nameMr": "बाबूलगाव"
  },
  {
    "id": "4128",
    "code": "4128",
    "districtId": "479",
    "nameEn": "Badnapur",
    "nameMr": "बदनापूर"
  },
  {
    "id": "4145",
    "code": "4145",
    "districtId": "487",
    "nameEn": "Baglan",
    "nameMr": "बागलण"
  },
  {
    "id": "3990",
    "code": "3990",
    "districtId": "467",
    "nameEn": "Balapur",
    "nameMr": "बाळापूर"
  },
  {
    "id": "4074",
    "code": "4074",
    "districtId": "473",
    "nameEn": "Ballarpur",
    "nameMr": "बल्लारपूर"
  },
  {
    "id": "4199",
    "code": "4199",
    "districtId": "490",
    "nameEn": "Baramati",
    "nameMr": "बारामती"
  },
  {
    "id": "4246",
    "code": "4246",
    "districtId": "496",
    "nameEn": "Barshi",
    "nameMr": "बार्शी"
  },
  {
    "id": "3994",
    "code": "3994",
    "districtId": "467",
    "nameEn": "Barshitakli",
    "nameMr": "बार्शीटाकळी"
  },
  {
    "id": "4221",
    "code": "4221",
    "districtId": "470",
    "nameEn": "Beed",
    "nameMr": "बीड"
  },
  {
    "id": "3971",
    "code": "3971",
    "districtId": "478",
    "nameEn": "Bhadgaon",
    "nameMr": "भडगाव"
  },
  {
    "id": "4070",
    "code": "4070",
    "districtId": "473",
    "nameEn": "Bhadravati",
    "nameMr": "भद्रावती"
  },
  {
    "id": "4061",
    "code": "4061",
    "districtId": "475",
    "nameEn": "Bhamragad",
    "nameMr": "भामरागड"
  },
  {
    "id": "4039",
    "code": "4039",
    "districtId": "471",
    "nameEn": "Bhandara",
    "nameMr": "भंडारा"
  },
  {
    "id": "4010",
    "code": "4010",
    "districtId": "468",
    "nameEn": "Bhatkuli",
    "nameMr": "भातकुली"
  },
  {
    "id": "4166",
    "code": "4166",
    "districtId": "497",
    "nameEn": "Bhiwandi",
    "nameMr": "भिवंडी"
  },
  {
    "id": "4036",
    "code": "4036",
    "districtId": "484",
    "nameEn": "Bhiwapur",
    "nameMr": "भिवापूर"
  },
  {
    "id": "4102",
    "code": "4102",
    "districtId": "485",
    "nameEn": "Bhokar",
    "nameMr": "भोकर"
  },
  {
    "id": "4125",
    "code": "4125",
    "districtId": "479",
    "nameEn": "Bhokardan",
    "nameMr": "भोकरदन"
  },
  {
    "id": "4198",
    "code": "4198",
    "districtId": "490",
    "nameEn": "Bhor",
    "nameMr": "भोर"
  },
  {
    "id": "4291",
    "code": "4291",
    "districtId": "480",
    "nameEn": "Bhudargad",
    "nameMr": "भूदरगड"
  },
  {
    "id": "4237",
    "code": "4237",
    "districtId": "488",
    "nameEn": "Bhum",
    "nameMr": "भूम"
  },
  {
    "id": "3965",
    "code": "3965",
    "districtId": "478",
    "nameEn": "Bhusawal",
    "nameMr": "भुसावळ"
  },
  {
    "id": "4105",
    "code": "4105",
    "districtId": "485",
    "nameEn": "Biloli",
    "nameMr": "बिलोली"
  },
  {
    "id": "3964",
    "code": "3964",
    "districtId": "478",
    "nameEn": "Bodvad",
    "nameMr": "बोधवड"
  },
  {
    "id": "7181",
    "code": "7181",
    "districtId": "483",
    "nameEn": "Borivali",
    "nameMr": "बोरीवली"
  },
  {
    "id": "4067",
    "code": "4067",
    "districtId": "473",
    "nameEn": "Brahmapuri",
    "nameMr": "ब्रम्हपुरी"
  },
  {
    "id": "3984",
    "code": "3984",
    "districtId": "472",
    "nameEn": "Buldana",
    "nameMr": "बुलडाणा"
  },
  {
    "id": "4230",
    "code": "4230",
    "districtId": "481",
    "nameEn": "Chakur",
    "nameMr": "चाकूर"
  },
  {
    "id": "3972",
    "code": "3972",
    "districtId": "478",
    "nameEn": "Chalisgaon",
    "nameMr": "चाळीसगाव"
  },
  {
    "id": "4058",
    "code": "4058",
    "districtId": "475",
    "nameEn": "Chamorshi",
    "nameMr": "चामोर्शी"
  },
  {
    "id": "4294",
    "code": "4294",
    "districtId": "480",
    "nameEn": "Chandgad",
    "nameMr": "चंदगड"
  },
  {
    "id": "4071",
    "code": "4071",
    "districtId": "473",
    "nameEn": "Chandrapur",
    "nameMr": "चंद्रपूर"
  },
  {
    "id": "4013",
    "code": "4013",
    "districtId": "468",
    "nameEn": "Chandur Railway",
    "nameMr": "चांदूर रेल्वे"
  },
  {
    "id": "4005",
    "code": "4005",
    "districtId": "468",
    "nameEn": "Chandurbazar",
    "nameMr": "चांदूर बाजार"
  },
  {
    "id": "4148",
    "code": "4148",
    "districtId": "487",
    "nameEn": "Chandvad",
    "nameMr": "चांदवड"
  },
  {
    "id": "4137",
    "code": "4137",
    "districtId": "469",
    "nameEn": "Chhatrapati Sambhajinagar",
    "nameMr": "छत्रपती संभाजीनगर"
  },
  {
    "id": "4002",
    "code": "4002",
    "districtId": "468",
    "nameEn": "Chikhaldara",
    "nameMr": "चिखलदरा"
  },
  {
    "id": "3983",
    "code": "3983",
    "districtId": "472",
    "nameEn": "Chikhli",
    "nameMr": "चिखली"
  },
  {
    "id": "4065",
    "code": "4065",
    "districtId": "473",
    "nameEn": "Chimur",
    "nameMr": "चिमूर"
  },
  {
    "id": "4269",
    "code": "4269",
    "districtId": "492",
    "nameEn": "Chiplun",
    "nameMr": "चिपळूण"
  },
  {
    "id": "3960",
    "code": "3960",
    "districtId": "478",
    "nameEn": "Chopda",
    "nameMr": "चोपडा"
  },
  {
    "id": "4158",
    "code": "4158",
    "districtId": "665",
    "nameEn": "Dahanu",
    "nameMr": "डहाणू"
  },
  {
    "id": "4267",
    "code": "4267",
    "districtId": "492",
    "nameEn": "Dapoli",
    "nameMr": "दापोली"
  },
  {
    "id": "4083",
    "code": "4083",
    "districtId": "500",
    "nameEn": "Darwha",
    "nameMr": "धारवा"
  },
  {
    "id": "4011",
    "code": "4011",
    "districtId": "468",
    "nameEn": "Daryapur",
    "nameMr": "दर्यापूर"
  },
  {
    "id": "4195",
    "code": "4195",
    "districtId": "490",
    "nameEn": "Daund",
    "nameMr": "दौंड"
  },
  {
    "id": "4110",
    "code": "4110",
    "districtId": "485",
    "nameEn": "Deglur",
    "nameMr": "देगलुर"
  },
  {
    "id": "4144",
    "code": "4144",
    "districtId": "487",
    "nameEn": "Deola",
    "nameMr": "देवळा"
  },
  {
    "id": "3985",
    "code": "3985",
    "districtId": "472",
    "nameEn": "Deolgaon Raja",
    "nameMr": "देउळगाव राजा"
  },
  {
    "id": "4020",
    "code": "4020",
    "districtId": "498",
    "nameEn": "Deoli",
    "nameMr": "देवळी"
  },
  {
    "id": "4234",
    "code": "4234",
    "districtId": "481",
    "nameEn": "Deoni",
    "nameMr": "देवनी"
  },
  {
    "id": "4051",
    "code": "4051",
    "districtId": "476",
    "nameEn": "Deori",
    "nameMr": "देवरी"
  },
  {
    "id": "4052",
    "code": "4052",
    "districtId": "475",
    "nameEn": "Desaiganj (Vadasa)",
    "nameMr": "देसाईगंज (वडसा)"
  },
  {
    "id": "4275",
    "code": "4275",
    "districtId": "495",
    "nameEn": "Devgad",
    "nameMr": "देवगड"
  },
  {
    "id": "4014",
    "code": "4014",
    "districtId": "468",
    "nameEn": "Dhamangaon Railway",
    "nameMr": "धामनगाव रेल्वे"
  },
  {
    "id": "4056",
    "code": "4056",
    "districtId": "475",
    "nameEn": "Dhanora",
    "nameMr": "धानोरा"
  },
  {
    "id": "3968",
    "code": "3968",
    "districtId": "478",
    "nameEn": "Dharangaon",
    "nameMr": "धरणगाव"
  },
  {
    "id": "4240",
    "code": "4240",
    "districtId": "488",
    "nameEn": "Dharashiv",
    "nameMr": "धाराशिव"
  },
  {
    "id": "4104",
    "code": "4104",
    "districtId": "485",
    "nameEn": "Dharmabad",
    "nameMr": "धर्माबाद"
  },
  {
    "id": "4001",
    "code": "4001",
    "districtId": "468",
    "nameEn": "Dharni",
    "nameMr": "धारणी"
  },
  {
    "id": "4223",
    "code": "4223",
    "districtId": "470",
    "nameEn": "Dharur",
    "nameMr": "धारूर"
  },
  {
    "id": "3959",
    "code": "3959",
    "districtId": "474",
    "nameEn": "Dhule",
    "nameMr": "धुळे"
  },
  {
    "id": "4084",
    "code": "4084",
    "districtId": "500",
    "nameEn": "Digras",
    "nameMr": "दिग्रस"
  },
  {
    "id": "4149",
    "code": "4149",
    "districtId": "487",
    "nameEn": "Dindori",
    "nameMr": "दिंडोरी"
  },
  {
    "id": "4282",
    "code": "4282",
    "districtId": "495",
    "nameEn": "Dodamarg",
    "nameMr": "दोडामार्ग"
  },
  {
    "id": "3967",
    "code": "3967",
    "districtId": "478",
    "nameEn": "Erandol",
    "nameMr": "एरंडोल"
  },
  {
    "id": "4060",
    "code": "4060",
    "districtId": "475",
    "nameEn": "Etapalli",
    "nameMr": "एटापल्ली"
  },
  {
    "id": "4057",
    "code": "4057",
    "districtId": "475",
    "nameEn": "Gadchiroli",
    "nameMr": "गडचिरोली"
  },
  {
    "id": "4293",
    "code": "4293",
    "districtId": "480",
    "nameEn": "Gadhinglaj",
    "nameMr": "गडहिंग्लज"
  },
  {
    "id": "4288",
    "code": "4288",
    "districtId": "480",
    "nameEn": "Gaganbawada",
    "nameMr": "गगनबावडा"
  },
  {
    "id": "4122",
    "code": "4122",
    "districtId": "489",
    "nameEn": "Gangakhed",
    "nameMr": "गंगाखेड"
  },
  {
    "id": "4140",
    "code": "4140",
    "districtId": "469",
    "nameEn": "Gangapur",
    "nameMr": "गंगापूर"
  },
  {
    "id": "4218",
    "code": "4218",
    "districtId": "470",
    "nameEn": "Georai",
    "nameMr": "गेवराई"
  },
  {
    "id": "4130",
    "code": "4130",
    "districtId": "479",
    "nameEn": "Ghansawangi",
    "nameMr": "घनसावंगी"
  },
  {
    "id": "4089",
    "code": "4089",
    "districtId": "500",
    "nameEn": "Ghatanji",
    "nameMr": "घाटंजी"
  },
  {
    "id": "4046",
    "code": "4046",
    "districtId": "476",
    "nameEn": "Gondiya",
    "nameMr": "गोंदिया"
  },
  {
    "id": "4078",
    "code": "4078",
    "districtId": "473",
    "nameEn": "Gondpipri",
    "nameMr": "गोंडपिपरी"
  },
  {
    "id": "4045",
    "code": "4045",
    "districtId": "476",
    "nameEn": "Goregaon",
    "nameMr": "गोरेगाव"
  },
  {
    "id": "4270",
    "code": "4270",
    "districtId": "492",
    "nameEn": "Guhagar",
    "nameMr": "गुहाघर"
  },
  {
    "id": "4098",
    "code": "4098",
    "districtId": "485",
    "nameEn": "Hadgaon",
    "nameMr": "हदगाव"
  },
  {
    "id": "4285",
    "code": "4285",
    "districtId": "480",
    "nameEn": "Hatkanangle",
    "nameMr": "हातकणंगले"
  },
  {
    "id": "4193",
    "code": "4193",
    "districtId": "490",
    "nameEn": "Haveli",
    "nameMr": "हवेली"
  },
  {
    "id": "4097",
    "code": "4097",
    "districtId": "485",
    "nameEn": "Himayatnagar",
    "nameMr": "हिमायतनगर"
  },
  {
    "id": "4021",
    "code": "4021",
    "districtId": "498",
    "nameEn": "Hinganghat",
    "nameMr": "हिंगणघाट"
  },
  {
    "id": "4033",
    "code": "4033",
    "districtId": "484",
    "nameEn": "Hingna",
    "nameMr": "हिंगणा"
  },
  {
    "id": "4112",
    "code": "4112",
    "districtId": "477",
    "nameEn": "Hingoli",
    "nameMr": "हिंगोली"
  },
  {
    "id": "4153",
    "code": "4153",
    "districtId": "487",
    "nameEn": "Igatpuri",
    "nameMr": "इगतपूरी"
  },
  {
    "id": "4200",
    "code": "4200",
    "districtId": "490",
    "nameEn": "Indapur",
    "nameMr": "इंदापूर"
  },
  {
    "id": "4126",
    "code": "4126",
    "districtId": "479",
    "nameEn": "Jafrabad",
    "nameMr": "जाफ्राबाद"
  },
  {
    "id": "3966",
    "code": "3966",
    "districtId": "478",
    "nameEn": "Jalgaon",
    "nameMr": "जळगाव"
  },
  {
    "id": "3975",
    "code": "3975",
    "districtId": "472",
    "nameEn": "Jalgaon (Jamod)",
    "nameMr": "जळगाव (जामोद)"
  },
  {
    "id": "4229",
    "code": "4229",
    "districtId": "481",
    "nameEn": "Jalkot",
    "nameMr": "जलकोट"
  },
  {
    "id": "4127",
    "code": "4127",
    "districtId": "479",
    "nameEn": "Jalna",
    "nameMr": "जालना"
  },
  {
    "id": "4214",
    "code": "4214",
    "districtId": "466",
    "nameEn": "Jamkhed",
    "nameMr": "जामखेड"
  },
  {
    "id": "3974",
    "code": "3974",
    "districtId": "478",
    "nameEn": "Jamner",
    "nameMr": "जामनेर"
  },
  {
    "id": "4263",
    "code": "4263",
    "districtId": "494",
    "nameEn": "Jaoli",
    "nameMr": "जावळी"
  },
  {
    "id": "4304",
    "code": "4304",
    "districtId": "493",
    "nameEn": "Jat",
    "nameMr": "जत"
  },
  {
    "id": "4160",
    "code": "4160",
    "districtId": "665",
    "nameEn": "Jawhar",
    "nameMr": "जव्हार"
  },
  {
    "id": "4117",
    "code": "4117",
    "districtId": "489",
    "nameEn": "Jintur",
    "nameMr": "जिंतुर"
  },
  {
    "id": "4076",
    "code": "4076",
    "districtId": "473",
    "nameEn": "Jiwati",
    "nameMr": "जिवती"
  },
  {
    "id": "4187",
    "code": "4187",
    "districtId": "490",
    "nameEn": "Junnar",
    "nameMr": "जुन्नर"
  },
  {
    "id": "4298",
    "code": "4298",
    "districtId": "493",
    "nameEn": "Kadegaon",
    "nameMr": "कडेगाव"
  },
  {
    "id": "4290",
    "code": "4290",
    "districtId": "480",
    "nameEn": "Kagal",
    "nameMr": "कागल"
  },
  {
    "id": "4222",
    "code": "4222",
    "districtId": "470",
    "nameEn": "Kaij",
    "nameMr": "केज"
  },
  {
    "id": "4239",
    "code": "4239",
    "districtId": "488",
    "nameEn": "Kalamb",
    "nameMr": "कळंब"
  },
  {
    "id": "4081",
    "code": "4081",
    "districtId": "500",
    "nameEn": "Kalamb",
    "nameMr": "कळंब"
  },
  {
    "id": "4025",
    "code": "4025",
    "districtId": "484",
    "nameEn": "Kalameshwar",
    "nameMr": "कळमेश्वर"
  },
  {
    "id": "4114",
    "code": "4114",
    "districtId": "477",
    "nameEn": "Kalamnuri",
    "nameMr": "कळमनुरी"
  },
  {
    "id": "4143",
    "code": "4143",
    "districtId": "487",
    "nameEn": "Kalwan",
    "nameMr": "कालवण"
  },
  {
    "id": "4168",
    "code": "4168",
    "districtId": "497",
    "nameEn": "Kalyan",
    "nameMr": "कल्याण"
  },
  {
    "id": "4030",
    "code": "4030",
    "districtId": "484",
    "nameEn": "Kamptee",
    "nameMr": "कामठी"
  },
  {
    "id": "4108",
    "code": "4108",
    "districtId": "485",
    "nameEn": "Kandhar",
    "nameMr": "कंधार"
  },
  {
    "id": "4277",
    "code": "4277",
    "districtId": "495",
    "nameEn": "Kankavli",
    "nameMr": "कणकवली"
  },
  {
    "id": "4133",
    "code": "4133",
    "districtId": "469",
    "nameEn": "Kannad",
    "nameMr": "कन्नड"
  },
  {
    "id": "4265",
    "code": "4265",
    "districtId": "494",
    "nameEn": "Karad",
    "nameMr": "कराड"
  },
  {
    "id": "4016",
    "code": "4016",
    "districtId": "498",
    "nameEn": "Karanja",
    "nameMr": "कारंजा"
  },
  {
    "id": "3997",
    "code": "3997",
    "districtId": "499",
    "nameEn": "Karanja",
    "nameMr": "कारंजा"
  },
  {
    "id": "4213",
    "code": "4213",
    "districtId": "466",
    "nameEn": "Karjat",
    "nameMr": "कर्जत"
  },
  {
    "id": "4174",
    "code": "4174",
    "districtId": "491",
    "nameEn": "Karjat",
    "nameMr": "कर्जत"
  },
  {
    "id": "4244",
    "code": "4244",
    "districtId": "496",
    "nameEn": "Karmala",
    "nameMr": "करमाळा"
  },
  {
    "id": "4287",
    "code": "4287",
    "districtId": "480",
    "nameEn": "Karvir",
    "nameMr": "करवीर"
  },
  {
    "id": "4024",
    "code": "4024",
    "districtId": "484",
    "nameEn": "Katol",
    "nameMr": "काटोल"
  },
  {
    "id": "4303",
    "code": "4303",
    "districtId": "493",
    "nameEn": "Kavathemahankal",
    "nameMr": "कवठेमहांकाळ"
  },
  {
    "id": "4090",
    "code": "4090",
    "districtId": "500",
    "nameEn": "Kelapur",
    "nameMr": "केलापूर"
  },
  {
    "id": "4175",
    "code": "4175",
    "districtId": "491",
    "nameEn": "Khalapur",
    "nameMr": "खालापूर"
  },
  {
    "id": "3981",
    "code": "3981",
    "districtId": "472",
    "nameEn": "Khamgaon",
    "nameMr": "खामगाव"
  },
  {
    "id": "4299",
    "code": "4299",
    "districtId": "493",
    "nameEn": "Khanapur",
    "nameMr": "खानापूर"
  },
  {
    "id": "4257",
    "code": "4257",
    "districtId": "494",
    "nameEn": "Khandala",
    "nameMr": "खंडाळा"
  },
  {
    "id": "4260",
    "code": "4260",
    "districtId": "494",
    "nameEn": "Khatav",
    "nameMr": "खटाव"
  },
  {
    "id": "4190",
    "code": "4190",
    "districtId": "490",
    "nameEn": "Khed",
    "nameMr": "खेड"
  },
  {
    "id": "4268",
    "code": "4268",
    "districtId": "492",
    "nameEn": "Khed",
    "nameMr": "खेड"
  },
  {
    "id": "4138",
    "code": "4138",
    "districtId": "469",
    "nameEn": "Khuldabad",
    "nameMr": "खुलताबाद"
  },
  {
    "id": "4096",
    "code": "4096",
    "districtId": "485",
    "nameEn": "Kinwat",
    "nameMr": "किनवत"
  },
  {
    "id": "4203",
    "code": "4203",
    "districtId": "466",
    "nameEn": "Kopargaon",
    "nameMr": "कोपरगाव"
  },
  {
    "id": "4055",
    "code": "4055",
    "districtId": "475",
    "nameEn": "Korchi",
    "nameMr": "कोरची"
  },
  {
    "id": "4261",
    "code": "4261",
    "districtId": "494",
    "nameEn": "Koregaon",
    "nameMr": "कोरेगाव"
  },
  {
    "id": "4075",
    "code": "4075",
    "districtId": "473",
    "nameEn": "Korpana",
    "nameMr": "कोरपना"
  },
  {
    "id": "4280",
    "code": "4280",
    "districtId": "495",
    "nameEn": "Kudal",
    "nameMr": "कुडाळ"
  },
  {
    "id": "4035",
    "code": "4035",
    "districtId": "484",
    "nameEn": "Kuhi",
    "nameMr": "कुही"
  },
  {
    "id": "4054",
    "code": "4054",
    "districtId": "475",
    "nameEn": "Kurkheda",
    "nameMr": "कुरखेडा"
  },
  {
    "id": "7180",
    "code": "7180",
    "districtId": "483",
    "nameEn": "Kurla",
    "nameMr": "कुर्ला"
  },
  {
    "id": "4043",
    "code": "4043",
    "districtId": "471",
    "nameEn": "Lakhandur",
    "nameMr": "लाखंदूर"
  },
  {
    "id": "4041",
    "code": "4041",
    "districtId": "471",
    "nameEn": "Lakhani",
    "nameMr": "लाखनी"
  },
  {
    "id": "4273",
    "code": "4273",
    "districtId": "492",
    "nameEn": "Lanja",
    "nameMr": "लांजा"
  },
  {
    "id": "4226",
    "code": "4226",
    "districtId": "481",
    "nameEn": "Latur",
    "nameMr": "लातूर"
  },
  {
    "id": "4107",
    "code": "4107",
    "districtId": "485",
    "nameEn": "Loha",
    "nameMr": "लोहा"
  },
  {
    "id": "4242",
    "code": "4242",
    "districtId": "488",
    "nameEn": "Lohara",
    "nameMr": "लोहारा"
  },
  {
    "id": "3987",
    "code": "3987",
    "districtId": "472",
    "nameEn": "Lonar",
    "nameMr": "लोणार"
  },
  {
    "id": "4245",
    "code": "4245",
    "districtId": "496",
    "nameEn": "Madha",
    "nameMr": "माढा"
  },
  {
    "id": "4255",
    "code": "4255",
    "districtId": "494",
    "nameEn": "Mahabaleshwar",
    "nameMr": "महाबळेश्वर"
  },
  {
    "id": "4185",
    "code": "4185",
    "districtId": "491",
    "nameEn": "Mahad",
    "nameMr": "महाड"
  },
  {
    "id": "4087",
    "code": "4087",
    "districtId": "500",
    "nameEn": "Mahagaon",
    "nameMr": "महागाव"
  },
  {
    "id": "4095",
    "code": "4095",
    "districtId": "485",
    "nameEn": "Mahur",
    "nameMr": "माहूर"
  },
  {
    "id": "4219",
    "code": "4219",
    "districtId": "470",
    "nameEn": "Majalgaon",
    "nameMr": "माजलगाव"
  },
  {
    "id": "3995",
    "code": "3995",
    "districtId": "499",
    "nameEn": "Malegaon",
    "nameMr": "मालेगाव"
  },
  {
    "id": "4146",
    "code": "4146",
    "districtId": "487",
    "nameEn": "Malegaon",
    "nameMr": "मालेगाव"
  },
  {
    "id": "3979",
    "code": "3979",
    "districtId": "472",
    "nameEn": "Malkapur",
    "nameMr": "मलकापूर"
  },
  {
    "id": "4250",
    "code": "4250",
    "districtId": "496",
    "nameEn": "Malshiras",
    "nameMr": "माळशिरस"
  },
  {
    "id": "4278",
    "code": "4278",
    "districtId": "495",
    "nameEn": "Malwan",
    "nameMr": "मालवण"
  },
  {
    "id": "4259",
    "code": "4259",
    "districtId": "494",
    "nameEn": "Man",
    "nameMr": "माण"
  },
  {
    "id": "4266",
    "code": "4266",
    "districtId": "492",
    "nameEn": "Mandangad",
    "nameMr": "मंडणगड"
  },
  {
    "id": "4252",
    "code": "4252",
    "districtId": "496",
    "nameEn": "Mangalvedhe",
    "nameMr": "मंगळवेढे"
  },
  {
    "id": "4181",
    "code": "4181",
    "districtId": "491",
    "nameEn": "Mangaon",
    "nameMr": "मानगाव"
  },
  {
    "id": "3996",
    "code": "3996",
    "districtId": "499",
    "nameEn": "Mangrulpir",
    "nameMr": "मंगरूळपीर"
  },
  {
    "id": "3998",
    "code": "3998",
    "districtId": "499",
    "nameEn": "Manora",
    "nameMr": "मानोरा"
  },
  {
    "id": "4132",
    "code": "4132",
    "districtId": "479",
    "nameEn": "Mantha",
    "nameMr": "मंठा"
  },
  {
    "id": "4119",
    "code": "4119",
    "districtId": "489",
    "nameEn": "Manwath",
    "nameMr": "मानवत"
  },
  {
    "id": "4092",
    "code": "4092",
    "districtId": "500",
    "nameEn": "Maregaon",
    "nameMr": "मारेगाव"
  },
  {
    "id": "4029",
    "code": "4029",
    "districtId": "484",
    "nameEn": "Mauda",
    "nameMr": "मौदा"
  },
  {
    "id": "4191",
    "code": "4191",
    "districtId": "490",
    "nameEn": "Mawal",
    "nameMr": "मावळ"
  },
  {
    "id": "3982",
    "code": "3982",
    "districtId": "472",
    "nameEn": "Mehkar",
    "nameMr": "मेहकर"
  },
  {
    "id": "4184",
    "code": "4184",
    "districtId": "491",
    "nameEn": "Mhasla",
    "nameMr": "म्हासळा"
  },
  {
    "id": "4302",
    "code": "4302",
    "districtId": "493",
    "nameEn": "Miraj",
    "nameMr": "मिरज"
  },
  {
    "id": "4038",
    "code": "4038",
    "districtId": "471",
    "nameEn": "Mohadi",
    "nameMr": "मोहाडी"
  },
  {
    "id": "4248",
    "code": "4248",
    "districtId": "496",
    "nameEn": "Mohol",
    "nameMr": "मोहोळ"
  },
  {
    "id": "4161",
    "code": "4161",
    "districtId": "665",
    "nameEn": "Mokhada",
    "nameMr": "मोखाडा"
  },
  {
    "id": "4006",
    "code": "4006",
    "districtId": "468",
    "nameEn": "Morshi",
    "nameMr": "मोर्शी"
  },
  {
    "id": "3980",
    "code": "3980",
    "districtId": "472",
    "nameEn": "Motala",
    "nameMr": "मोताळा"
  },
  {
    "id": "4101",
    "code": "4101",
    "districtId": "485",
    "nameEn": "Mudkhed",
    "nameMr": "मुदखेड"
  },
  {
    "id": "4109",
    "code": "4109",
    "districtId": "485",
    "nameEn": "Mukhed",
    "nameMr": "मुखेड"
  },
  {
    "id": "3963",
    "code": "3963",
    "districtId": "478",
    "nameEn": "Muktainagar (Edlabad)",
    "nameMr": "मुक्ताईनगर(एदलाबाद)"
  },
  {
    "id": "4072",
    "code": "4072",
    "districtId": "473",
    "nameEn": "Mul",
    "nameMr": "मुल"
  },
  {
    "id": "4059",
    "code": "4059",
    "districtId": "475",
    "nameEn": "Mulchera",
    "nameMr": "मुलचेरा"
  },
  {
    "id": "4192",
    "code": "4192",
    "districtId": "490",
    "nameEn": "Mulshi",
    "nameMr": "मुळशी"
  },
  {
    "id": "4171",
    "code": "4171",
    "districtId": "497",
    "nameEn": "Murbad",
    "nameMr": "मुरबाड"
  },
  {
    "id": "3992",
    "code": "3992",
    "districtId": "467",
    "nameEn": "Murtijapur",
    "nameMr": "मुर्तिजापूर"
  },
  {
    "id": "4178",
    "code": "4178",
    "districtId": "491",
    "nameEn": "Murud",
    "nameMr": "मुरूड"
  },
  {
    "id": "4209",
    "code": "4209",
    "districtId": "466",
    "nameEn": "Nagar",
    "nameMr": "नगर"
  },
  {
    "id": "4066",
    "code": "4066",
    "districtId": "473",
    "nameEn": "Nagbhid",
    "nameMr": "नागभिड"
  },
  {
    "id": "4031",
    "code": "4031",
    "districtId": "484",
    "nameEn": "Nagpur (Rural)",
    "nameMr": "नागपूर (ग्रामीण)"
  },
  {
    "id": "4032",
    "code": "4032",
    "districtId": "484",
    "nameEn": "Nagpur (Urban)",
    "nameMr": "नागपूर (शहर)"
  },
  {
    "id": "4106",
    "code": "4106",
    "districtId": "485",
    "nameEn": "Naigaon (Khairgaon)",
    "nameMr": "नायगाव (खैरगाव)"
  },
  {
    "id": "4100",
    "code": "4100",
    "districtId": "485",
    "nameEn": "Nanded",
    "nameMr": "नांदेड"
  },
  {
    "id": "4147",
    "code": "4147",
    "districtId": "487",
    "nameEn": "Nandgaon",
    "nameMr": "नांदगाव"
  },
  {
    "id": "4012",
    "code": "4012",
    "districtId": "468",
    "nameEn": "Nandgaon-Khandeshwar",
    "nameMr": "नांदगाव खंडेश्वर"
  },
  {
    "id": "3978",
    "code": "3978",
    "districtId": "472",
    "nameEn": "Nandura",
    "nameMr": "नांदुरा"
  },
  {
    "id": "3954",
    "code": "3954",
    "districtId": "486",
    "nameEn": "Nandurbar",
    "nameMr": "नंदूरबार"
  },
  {
    "id": "4023",
    "code": "4023",
    "districtId": "484",
    "nameEn": "Narkhed",
    "nameMr": "नरखेड"
  },
  {
    "id": "4152",
    "code": "4152",
    "districtId": "487",
    "nameEn": "Nashik",
    "nameMr": "नाशिक"
  },
  {
    "id": "3955",
    "code": "3955",
    "districtId": "486",
    "nameEn": "Nawapur",
    "nameMr": "नवापूर"
  },
  {
    "id": "4079",
    "code": "4079",
    "districtId": "500",
    "nameEn": "Ner",
    "nameMr": "नेर"
  },
  {
    "id": "4206",
    "code": "4206",
    "districtId": "466",
    "nameEn": "Nevasa",
    "nameMr": "नेवासा"
  },
  {
    "id": "4233",
    "code": "4233",
    "districtId": "481",
    "nameEn": "Nilanga",
    "nameMr": "निलंगा"
  },
  {
    "id": "4155",
    "code": "4155",
    "districtId": "487",
    "nameEn": "Niphad",
    "nameMr": "निफाड"
  },
  {
    "id": "4243",
    "code": "4243",
    "districtId": "488",
    "nameEn": "Omarga",
    "nameMr": "उमरगा"
  },
  {
    "id": "3973",
    "code": "3973",
    "districtId": "478",
    "nameEn": "Pachora",
    "nameMr": "पाचोरा"
  },
  {
    "id": "4141",
    "code": "4141",
    "districtId": "469",
    "nameEn": "Paithan",
    "nameMr": "पैठण"
  },
  {
    "id": "4123",
    "code": "4123",
    "districtId": "489",
    "nameEn": "Palam",
    "nameMr": "पालम"
  },
  {
    "id": "4163",
    "code": "4163",
    "districtId": "665",
    "nameEn": "Palghar",
    "nameMr": "पालघर"
  },
  {
    "id": "4297",
    "code": "4297",
    "districtId": "493",
    "nameEn": "Palus",
    "nameMr": "पलुस"
  },
  {
    "id": "4249",
    "code": "4249",
    "districtId": "496",
    "nameEn": "Pandharpur",
    "nameMr": "पंढरपूर"
  },
  {
    "id": "4284",
    "code": "4284",
    "districtId": "480",
    "nameEn": "Panhala",
    "nameMr": "पन्हाळा"
  },
  {
    "id": "4173",
    "code": "4173",
    "districtId": "491",
    "nameEn": "Panvel",
    "nameMr": "पनवेल"
  },
  {
    "id": "4236",
    "code": "4236",
    "districtId": "488",
    "nameEn": "Paranda",
    "nameMr": "परंडा"
  },
  {
    "id": "4118",
    "code": "4118",
    "districtId": "489",
    "nameEn": "Parbhani",
    "nameMr": "परभणी"
  },
  {
    "id": "4224",
    "code": "4224",
    "districtId": "470",
    "nameEn": "Parli",
    "nameMr": "परळी"
  },
  {
    "id": "4211",
    "code": "4211",
    "districtId": "466",
    "nameEn": "Parner",
    "nameMr": "पारनेर"
  },
  {
    "id": "3970",
    "code": "3970",
    "districtId": "478",
    "nameEn": "Parola",
    "nameMr": "पारोळा"
  },
  {
    "id": "4027",
    "code": "4027",
    "districtId": "484",
    "nameEn": "Parseoni",
    "nameMr": "पारशिवनी"
  },
  {
    "id": "4131",
    "code": "4131",
    "districtId": "479",
    "nameEn": "Partur",
    "nameMr": "परतूर"
  },
  {
    "id": "4264",
    "code": "4264",
    "districtId": "494",
    "nameEn": "Patan",
    "nameMr": "पाटण"
  },
  {
    "id": "4208",
    "code": "4208",
    "districtId": "466",
    "nameEn": "Pathardi",
    "nameMr": "पाथर्डी"
  },
  {
    "id": "4120",
    "code": "4120",
    "districtId": "489",
    "nameEn": "Pathri",
    "nameMr": "पाथ्री"
  },
  {
    "id": "4216",
    "code": "4216",
    "districtId": "470",
    "nameEn": "Patoda",
    "nameMr": "पाटोदा"
  },
  {
    "id": "3993",
    "code": "3993",
    "districtId": "467",
    "nameEn": "Patur",
    "nameMr": "पातूर"
  },
  {
    "id": "4042",
    "code": "4042",
    "districtId": "471",
    "nameEn": "Pauni",
    "nameMr": "पवनी"
  },
  {
    "id": "4176",
    "code": "4176",
    "districtId": "491",
    "nameEn": "Pen",
    "nameMr": "पेण"
  },
  {
    "id": "4150",
    "code": "4150",
    "districtId": "487",
    "nameEn": "Peth",
    "nameMr": "पेठ"
  },
  {
    "id": "4258",
    "code": "4258",
    "districtId": "494",
    "nameEn": "Phaltan",
    "nameMr": "फलटण"
  },
  {
    "id": "4136",
    "code": "4136",
    "districtId": "469",
    "nameEn": "Phulambri",
    "nameMr": "फुलंब्री"
  },
  {
    "id": "4186",
    "code": "4186",
    "districtId": "491",
    "nameEn": "Poladpur",
    "nameMr": "पोलादपूर"
  },
  {
    "id": "4073",
    "code": "4073",
    "districtId": "473",
    "nameEn": "Pombhurna",
    "nameMr": "पोम्भूर्णा"
  },
  {
    "id": "4194",
    "code": "4194",
    "districtId": "490",
    "nameEn": "Pune City",
    "nameMr": "पुणे शहर"
  },
  {
    "id": "4196",
    "code": "4196",
    "districtId": "490",
    "nameEn": "Purandhar",
    "nameMr": "पुरंदर"
  },
  {
    "id": "4124",
    "code": "4124",
    "districtId": "489",
    "nameEn": "Purna",
    "nameMr": "पुर्णा"
  },
  {
    "id": "4085",
    "code": "4085",
    "districtId": "500",
    "nameEn": "Pusad",
    "nameMr": "पूसद"
  },
  {
    "id": "4289",
    "code": "4289",
    "districtId": "480",
    "nameEn": "Radhanagari",
    "nameMr": "राधानगरी"
  },
  {
    "id": "4204",
    "code": "4204",
    "districtId": "466",
    "nameEn": "Rahta",
    "nameMr": "रहाता"
  },
  {
    "id": "4210",
    "code": "4210",
    "districtId": "466",
    "nameEn": "Rahuri",
    "nameMr": "राहुरी"
  },
  {
    "id": "4274",
    "code": "4274",
    "districtId": "492",
    "nameEn": "Rajapur",
    "nameMr": "राजापूर"
  },
  {
    "id": "4077",
    "code": "4077",
    "districtId": "473",
    "nameEn": "Rajura",
    "nameMr": "राजुरा"
  },
  {
    "id": "4091",
    "code": "4091",
    "districtId": "500",
    "nameEn": "Ralegaon",
    "nameMr": "राळेगाव"
  },
  {
    "id": "4028",
    "code": "4028",
    "districtId": "484",
    "nameEn": "Ramtek",
    "nameMr": "रामटेक"
  },
  {
    "id": "4271",
    "code": "4271",
    "districtId": "492",
    "nameEn": "Ratnagiri",
    "nameMr": "रत्नागिरी"
  },
  {
    "id": "3962",
    "code": "3962",
    "districtId": "478",
    "nameEn": "Raver",
    "nameMr": "रावेर"
  },
  {
    "id": "4227",
    "code": "4227",
    "districtId": "481",
    "nameEn": "Renapur",
    "nameMr": "रेनापूर"
  },
  {
    "id": "4000",
    "code": "4000",
    "districtId": "499",
    "nameEn": "Risod",
    "nameMr": "रिसोड"
  },
  {
    "id": "4179",
    "code": "4179",
    "districtId": "491",
    "nameEn": "Roha",
    "nameMr": "रोहा"
  },
  {
    "id": "4049",
    "code": "4049",
    "districtId": "476",
    "nameEn": "Sadak-Arjuni",
    "nameMr": "सडक अर्जुनी"
  },
  {
    "id": "4040",
    "code": "4040",
    "districtId": "471",
    "nameEn": "Sakoli",
    "nameMr": "साकोली"
  },
  {
    "id": "3958",
    "code": "3958",
    "districtId": "474",
    "nameEn": "Sakri",
    "nameMr": "साक्री"
  },
  {
    "id": "4048",
    "code": "4048",
    "districtId": "476",
    "nameEn": "Salekasa",
    "nameMr": "सालेकसा"
  },
  {
    "id": "4022",
    "code": "4022",
    "districtId": "498",
    "nameEn": "Samudrapur",
    "nameMr": "समुद्रपूर"
  },
  {
    "id": "4272",
    "code": "4272",
    "districtId": "492",
    "nameEn": "Sangameshwar",
    "nameMr": "संगमेश्वर"
  },
  {
    "id": "4202",
    "code": "4202",
    "districtId": "466",
    "nameEn": "Sangamner",
    "nameMr": "संगमनेर"
  },
  {
    "id": "4251",
    "code": "4251",
    "districtId": "496",
    "nameEn": "Sangole",
    "nameMr": "सांगोले"
  },
  {
    "id": "3976",
    "code": "3976",
    "districtId": "472",
    "nameEn": "Sangrampur",
    "nameMr": "संग्रामपूर"
  },
  {
    "id": "4262",
    "code": "4262",
    "districtId": "494",
    "nameEn": "Satara",
    "nameMr": "सातारा"
  },
  {
    "id": "4026",
    "code": "4026",
    "districtId": "484",
    "nameEn": "Savner",
    "nameMr": "सावनेर"
  },
  {
    "id": "4068",
    "code": "4068",
    "districtId": "473",
    "nameEn": "Sawali",
    "nameMr": "सावली"
  },
  {
    "id": "4281",
    "code": "4281",
    "districtId": "495",
    "nameEn": "Sawantwadi",
    "nameMr": "सावंतवाडी"
  },
  {
    "id": "4018",
    "code": "4018",
    "districtId": "498",
    "nameEn": "Seloo",
    "nameMr": "सेलू"
  },
  {
    "id": "4116",
    "code": "4116",
    "districtId": "489",
    "nameEn": "Selu",
    "nameMr": "सेलू"
  },
  {
    "id": "4111",
    "code": "4111",
    "districtId": "477",
    "nameEn": "Sengaon",
    "nameMr": "सेनगाव"
  },
  {
    "id": "3953",
    "code": "3953",
    "districtId": "486",
    "nameEn": "Shahade",
    "nameMr": "शहादा"
  },
  {
    "id": "4167",
    "code": "4167",
    "districtId": "497",
    "nameEn": "Shahapur",
    "nameMr": "शहापूर"
  },
  {
    "id": "4283",
    "code": "4283",
    "districtId": "480",
    "nameEn": "Shahuwadi",
    "nameMr": "शाहूवाडी"
  },
  {
    "id": "3977",
    "code": "3977",
    "districtId": "472",
    "nameEn": "Shegaon",
    "nameMr": "शेगाव"
  },
  {
    "id": "4207",
    "code": "4207",
    "districtId": "466",
    "nameEn": "Shevgaon",
    "nameMr": "शेगाव"
  },
  {
    "id": "4295",
    "code": "4295",
    "districtId": "493",
    "nameEn": "Shirala",
    "nameMr": "शिराळा"
  },
  {
    "id": "4286",
    "code": "4286",
    "districtId": "480",
    "nameEn": "Shirol",
    "nameMr": "शिरोळ"
  },
  {
    "id": "3956",
    "code": "3956",
    "districtId": "474",
    "nameEn": "Shirpur",
    "nameMr": "शिरपूर"
  },
  {
    "id": "4189",
    "code": "4189",
    "districtId": "490",
    "nameEn": "Shirur",
    "nameMr": "शिरूर"
  },
  {
    "id": "4217",
    "code": "4217",
    "districtId": "470",
    "nameEn": "Shirur (Kasar)",
    "nameMr": "शिरूर (कासार)"
  },
  {
    "id": "4231",
    "code": "4231",
    "districtId": "481",
    "nameEn": "Shirur Anantpal",
    "nameMr": "शिरूर अनंतपाळ"
  },
  {
    "id": "4212",
    "code": "4212",
    "districtId": "466",
    "nameEn": "Shrigonda",
    "nameMr": "श्रीगोंदा"
  },
  {
    "id": "4205",
    "code": "4205",
    "districtId": "466",
    "nameEn": "Shrirampur",
    "nameMr": "श्रीरामपूर"
  },
  {
    "id": "4183",
    "code": "4183",
    "districtId": "491",
    "nameEn": "Shrivardhan",
    "nameMr": "श्रीवर्धन"
  },
  {
    "id": "4135",
    "code": "4135",
    "districtId": "469",
    "nameEn": "Sillod",
    "nameMr": "सिल्लोड"
  },
  {
    "id": "4069",
    "code": "4069",
    "districtId": "473",
    "nameEn": "Sindewahi",
    "nameMr": "सिंदेवाही"
  },
  {
    "id": "3986",
    "code": "3986",
    "districtId": "472",
    "nameEn": "Sindkhed Raja",
    "nameMr": "सिंदखेड राजा"
  },
  {
    "id": "3957",
    "code": "3957",
    "districtId": "474",
    "nameEn": "Sindkhede",
    "nameMr": "सिंदखेडे"
  },
  {
    "id": "4154",
    "code": "4154",
    "districtId": "487",
    "nameEn": "Sinnar",
    "nameMr": "सिन्नर"
  },
  {
    "id": "4063",
    "code": "4063",
    "districtId": "475",
    "nameEn": "Sironcha",
    "nameMr": "सिरोंचा"
  },
  {
    "id": "4134",
    "code": "4134",
    "districtId": "469",
    "nameEn": "Soegaon",
    "nameMr": "सोयगाव"
  },
  {
    "id": "4247",
    "code": "4247",
    "districtId": "496",
    "nameEn": "Solapur North",
    "nameMr": "उत्तर सोलापूर"
  },
  {
    "id": "4253",
    "code": "4253",
    "districtId": "496",
    "nameEn": "Solapur South",
    "nameMr": "दक्षिण सोलापूर"
  },
  {
    "id": "4121",
    "code": "4121",
    "districtId": "489",
    "nameEn": "Sonpeth",
    "nameMr": "सोनपेठ"
  },
  {
    "id": "4180",
    "code": "4180",
    "districtId": "491",
    "nameEn": "Sudhagad",
    "nameMr": "सुधागड"
  },
  {
    "id": "4142",
    "code": "4142",
    "districtId": "487",
    "nameEn": "Surgana",
    "nameMr": "सुरगना"
  },
  {
    "id": "4182",
    "code": "4182",
    "districtId": "491",
    "nameEn": "Tala",
    "nameMr": "तळा"
  },
  {
    "id": "4157",
    "code": "4157",
    "districtId": "665",
    "nameEn": "Talasari",
    "nameMr": "तलासरी"
  },
  {
    "id": "3952",
    "code": "3952",
    "districtId": "486",
    "nameEn": "Talode",
    "nameMr": "तलोदा"
  },
  {
    "id": "4301",
    "code": "4301",
    "districtId": "493",
    "nameEn": "Tasgaon",
    "nameMr": "तासगाव"
  },
  {
    "id": "3988",
    "code": "3988",
    "districtId": "467",
    "nameEn": "Telhara",
    "nameMr": "तेल्हारा"
  },
  {
    "id": "4165",
    "code": "4165",
    "districtId": "497",
    "nameEn": "Thane",
    "nameMr": "ठाणे"
  },
  {
    "id": "4008",
    "code": "4008",
    "districtId": "468",
    "nameEn": "Tiosa",
    "nameMr": "तिवसा"
  },
  {
    "id": "4044",
    "code": "4044",
    "districtId": "476",
    "nameEn": "Tirora",
    "nameMr": "तिरोडा"
  },
  {
    "id": "4151",
    "code": "4151",
    "districtId": "487",
    "nameEn": "Trimbakeshwar",
    "nameMr": "त्र्यंबक"
  },
  {
    "id": "4241",
    "code": "4241",
    "districtId": "488",
    "nameEn": "Tuljapur",
    "nameMr": "तुळजापूर"
  },
  {
    "id": "4037",
    "code": "4037",
    "districtId": "471",
    "nameEn": "Tumsar",
    "nameMr": "तुमसर"
  },
  {
    "id": "4235",
    "code": "4235",
    "districtId": "481",
    "nameEn": "Udgir",
    "nameMr": "उदगीर"
  },
  {
    "id": "4169",
    "code": "4169",
    "districtId": "497",
    "nameEn": "Ulhasnagar",
    "nameMr": "उल्हासनगर"
  },
  {
    "id": "4086",
    "code": "4086",
    "districtId": "500",
    "nameEn": "Umarkhed",
    "nameMr": "उमरखेड"
  },
  {
    "id": "4034",
    "code": "4034",
    "districtId": "484",
    "nameEn": "Umred",
    "nameMr": "उमरेड"
  },
  {
    "id": "4103",
    "code": "4103",
    "districtId": "485",
    "nameEn": "Umri",
    "nameMr": "उमरी"
  },
  {
    "id": "4172",
    "code": "4172",
    "districtId": "491",
    "nameEn": "Uran",
    "nameMr": "उरण"
  },
  {
    "id": "4276",
    "code": "4276",
    "districtId": "495",
    "nameEn": "Vaibhavvadi",
    "nameMr": "वैभववाडी"
  },
  {
    "id": "4139",
    "code": "4139",
    "districtId": "469",
    "nameEn": "Vaijapur",
    "nameMr": "वैजापूर"
  },
  {
    "id": "4164",
    "code": "4164",
    "districtId": "665",
    "nameEn": "Vasai",
    "nameMr": "वसई"
  },
  {
    "id": "4115",
    "code": "4115",
    "districtId": "477",
    "nameEn": "Vasmath",
    "nameMr": "वसमत"
  },
  {
    "id": "4197",
    "code": "4197",
    "districtId": "490",
    "nameEn": "Velhe",
    "nameMr": "वेल्हे"
  },
  {
    "id": "4279",
    "code": "4279",
    "districtId": "495",
    "nameEn": "Vengurla",
    "nameMr": "वेंगुर्ला"
  },
  {
    "id": "4159",
    "code": "4159",
    "districtId": "665",
    "nameEn": "Vikramgad",
    "nameMr": "विक्रमगड"
  },
  {
    "id": "4162",
    "code": "4162",
    "districtId": "665",
    "nameEn": "Wada",
    "nameMr": "वाडा"
  },
  {
    "id": "4220",
    "code": "4220",
    "districtId": "470",
    "nameEn": "Wadwani",
    "nameMr": "वडवणी"
  },
  {
    "id": "4256",
    "code": "4256",
    "districtId": "494",
    "nameEn": "Wai",
    "nameMr": "वाई"
  },
  {
    "id": "4296",
    "code": "4296",
    "districtId": "493",
    "nameEn": "Walwa",
    "nameMr": "वाळवा"
  },
  {
    "id": "4094",
    "code": "4094",
    "districtId": "500",
    "nameEn": "Wani",
    "nameMr": "वनी"
  },
  {
    "id": "4019",
    "code": "4019",
    "districtId": "498",
    "nameEn": "Wardha",
    "nameMr": "वर्धा"
  },
  {
    "id": "4064",
    "code": "4064",
    "districtId": "473",
    "nameEn": "Warora",
    "nameMr": "वरोरा"
  },
  {
    "id": "4007",
    "code": "4007",
    "districtId": "468",
    "nameEn": "Warud",
    "nameMr": "वरूड"
  },
  {
    "id": "4238",
    "code": "4238",
    "districtId": "488",
    "nameEn": "Washi",
    "nameMr": "वाशी"
  },
  {
    "id": "3999",
    "code": "3999",
    "districtId": "499",
    "nameEn": "Washim",
    "nameMr": "वाशिम"
  },
  {
    "id": "4082",
    "code": "4082",
    "districtId": "500",
    "nameEn": "Yavatmal",
    "nameMr": "यवतमाळ"
  },
  {
    "id": "3961",
    "code": "3961",
    "districtId": "478",
    "nameEn": "Yawal",
    "nameMr": "यावल"
  },
  {
    "id": "4156",
    "code": "4156",
    "districtId": "487",
    "nameEn": "Yevla",
    "nameMr": "येवला"
  },
  {
    "id": "4093",
    "code": "4093",
    "districtId": "500",
    "nameEn": "Zari-Jamani",
    "nameMr": "जरी जामनी"
  }
];

// 3. PRE-SEEDED CHIKHLI (TALUKA 3983 - BULDHANA) GRAM PANCHAYATS & VILLAGES
// Preserves Buldhana -> Chikhli -> Palaskhed Daulat (GP 172627) synchronously
export const chikhliPreloadedGps: GramPanchayat[] = [
  {
    "id": "172561",
    "code": "172561",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Ambashi",
    "nameMr": "अंबाशी",
    "pincode": ""
  },
  {
    "id": "172562",
    "code": "172562",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Amdapur",
    "nameMr": "अमडापुर",
    "pincode": ""
  },
  {
    "id": "172563",
    "code": "172563",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Amkhed",
    "nameMr": "आमखेड",
    "pincode": ""
  },
  {
    "id": "172564",
    "code": "172564",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Amona",
    "nameMr": "अमोना",
    "pincode": ""
  },
  {
    "id": "172565",
    "code": "172565",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Ancharwadi",
    "nameMr": "अंचरवाडी",
    "pincode": ""
  },
  {
    "id": "172566",
    "code": "172566",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Andhai",
    "nameMr": "आधंई",
    "pincode": ""
  },
  {
    "id": "172567",
    "code": "172567",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Antri Khedekar",
    "nameMr": "अंत्रीखेडेकर",
    "pincode": ""
  },
  {
    "id": "172568",
    "code": "172568",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Antri Koli",
    "nameMr": "अंत्रीकोळी",
    "pincode": ""
  },
  {
    "id": "172569",
    "code": "172569",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Asola Prkherda",
    "nameMr": "असोला प्रखेरदा",
    "pincode": ""
  },
  {
    "id": "172570",
    "code": "172570",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Berala",
    "nameMr": "बेराळा",
    "pincode": ""
  },
  {
    "id": "172571",
    "code": "172571",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Bhalgaon",
    "nameMr": "भालगांव",
    "pincode": ""
  },
  {
    "id": "172572",
    "code": "172572",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Bhankhed",
    "nameMr": "भानखेड",
    "pincode": ""
  },
  {
    "id": "172573",
    "code": "172573",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Bharosa",
    "nameMr": "भरोसा",
    "pincode": ""
  },
  {
    "id": "172574",
    "code": "172574",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Bhogawati",
    "nameMr": "भोगावती",
    "pincode": ""
  },
  {
    "id": "172575",
    "code": "172575",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Bhokar",
    "nameMr": "भोकर",
    "pincode": ""
  },
  {
    "id": "172576",
    "code": "172576",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Bhorsa",
    "nameMr": "भरोसा",
    "pincode": ""
  },
  {
    "id": "172577",
    "code": "172577",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Borgaon Kakade",
    "nameMr": "बोरगावकाकडे",
    "pincode": ""
  },
  {
    "id": "172578",
    "code": "172578",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Borgaonwasu",
    "nameMr": "बोरगाव वसू",
    "pincode": ""
  },
  {
    "id": "172579",
    "code": "172579",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Bramhapuri",
    "nameMr": "ब्रम्हपुरी",
    "pincode": ""
  },
  {
    "id": "172580",
    "code": "172580",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Chandanpur",
    "nameMr": "चंदनपुर",
    "pincode": ""
  },
  {
    "id": "172581",
    "code": "172581",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Chandhaiprchikhli",
    "nameMr": "चानधैप्राचीकली",
    "pincode": ""
  },
  {
    "id": "172582",
    "code": "172582",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Dahigaon",
    "nameMr": "दहीगाव",
    "pincode": ""
  },
  {
    "id": "172583",
    "code": "172583",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Dasala",
    "nameMr": "डासाला",
    "pincode": ""
  },
  {
    "id": "172584",
    "code": "172584",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Deulgaondhangar",
    "nameMr": "देऊळगाव धनगर",
    "pincode": ""
  },
  {
    "id": "172585",
    "code": "172585",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Dhanori",
    "nameMr": "धानोरी",
    "pincode": ""
  },
  {
    "id": "172586",
    "code": "172586",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Dhodap",
    "nameMr": "धोडप",
    "pincode": ""
  },
  {
    "id": "172587",
    "code": "172587",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Dhotra Bhangoji",
    "nameMr": "धोत्रा भांगोजी",
    "pincode": ""
  },
  {
    "id": "172588",
    "code": "172588",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Dhotra Naik",
    "nameMr": "धोत्रा नाईक",
    "pincode": ""
  },
  {
    "id": "172589",
    "code": "172589",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Diwathana",
    "nameMr": "दिवठाना",
    "pincode": ""
  },
  {
    "id": "172590",
    "code": "172590",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Dongar Shewali",
    "nameMr": "डोंगरशेवली",
    "pincode": ""
  },
  {
    "id": "172591",
    "code": "172591",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Dongargaon",
    "nameMr": "डोंगरगाव",
    "pincode": ""
  },
  {
    "id": "172592",
    "code": "172592",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Eklara",
    "nameMr": "एकलारा",
    "pincode": ""
  },
  {
    "id": "172593",
    "code": "172593",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Gangalgaon",
    "nameMr": "गागंलगाव",
    "pincode": ""
  },
  {
    "id": "172594",
    "code": "172594",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Godri",
    "nameMr": "गोद्री",
    "pincode": ""
  },
  {
    "id": "172678",
    "code": "172678",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Gunjala",
    "nameMr": "गुंजाळा",
    "pincode": ""
  },
  {
    "id": "172595",
    "code": "172595",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Harni",
    "nameMr": "हरणी",
    "pincode": ""
  },
  {
    "id": "172596",
    "code": "172596",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Hatni",
    "nameMr": "हातनी",
    "pincode": ""
  },
  {
    "id": "172597",
    "code": "172597",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Isoli",
    "nameMr": "इसोली",
    "pincode": ""
  },
  {
    "id": "172598",
    "code": "172598",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Isrul",
    "nameMr": "इसरूळ",
    "pincode": ""
  },
  {
    "id": "172599",
    "code": "172599",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Karankhed",
    "nameMr": "करणखेड",
    "pincode": ""
  },
  {
    "id": "172600",
    "code": "172600",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Karatwadi",
    "nameMr": "करतवाडी",
    "pincode": ""
  },
  {
    "id": "172601",
    "code": "172601",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Karwand",
    "nameMr": "करवंड",
    "pincode": ""
  },
  {
    "id": "172602",
    "code": "172602",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Katoda",
    "nameMr": "काटोडा",
    "pincode": ""
  },
  {
    "id": "172603",
    "code": "172603",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Kavhala",
    "nameMr": "कव्हळा",
    "pincode": ""
  },
  {
    "id": "172604",
    "code": "172604",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Kawathal",
    "nameMr": "कवठळ",
    "pincode": ""
  },
  {
    "id": "172605",
    "code": "172605",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Kelwad",
    "nameMr": "केळवद",
    "pincode": ""
  },
  {
    "id": "172606",
    "code": "172606",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Khairav",
    "nameMr": "खैरव",
    "pincode": ""
  },
  {
    "id": "172607",
    "code": "172607",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Khandala Makardhwaj",
    "nameMr": "खंडाळामकरध्वज",
    "pincode": ""
  },
  {
    "id": "172608",
    "code": "172608",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Khor",
    "nameMr": "खोर",
    "pincode": ""
  },
  {
    "id": "172609",
    "code": "172609",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Kinhi Naik",
    "nameMr": "किन्ही नाईक",
    "pincode": ""
  },
  {
    "id": "172610",
    "code": "172610",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Kinhi Sawadat",
    "nameMr": "किन्ही सवडत",
    "pincode": ""
  },
  {
    "id": "172611",
    "code": "172611",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Kinhola",
    "nameMr": "किन्होळा",
    "pincode": ""
  },
  {
    "id": "172612",
    "code": "172612",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Kolara",
    "nameMr": "कोलारा",
    "pincode": ""
  },
  {
    "id": "172613",
    "code": "172613",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Konad",
    "nameMr": "कोनंड",
    "pincode": ""
  },
  {
    "id": "172614",
    "code": "172614",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Mahimal",
    "nameMr": "महिमळ",
    "pincode": ""
  },
  {
    "id": "172615",
    "code": "172615",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Malgani",
    "nameMr": "मालगणी",
    "pincode": ""
  },
  {
    "id": "172616",
    "code": "172616",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Malgi",
    "nameMr": "मलगी",
    "pincode": ""
  },
  {
    "id": "172617",
    "code": "172617",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Malshemba",
    "nameMr": "माळशेबा",
    "pincode": ""
  },
  {
    "id": "172619",
    "code": "172619",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Mangrul Nawghare",
    "nameMr": "मंगरूळ नवघरे",
    "pincode": ""
  },
  {
    "id": "172618",
    "code": "172618",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Mangrul Pr Kherda",
    "nameMr": "मंगरूळ प्र खेर्डा",
    "pincode": ""
  },
  {
    "id": "172682",
    "code": "172682",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Manubai",
    "nameMr": "मनुबाई",
    "pincode": ""
  },
  {
    "id": "172620",
    "code": "172620",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Mera Bk",
    "nameMr": "मेरा बु.",
    "pincode": ""
  },
  {
    "id": "172621",
    "code": "172621",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Mera Kh",
    "nameMr": "मेरा खु.",
    "pincode": ""
  },
  {
    "id": "172622",
    "code": "172622",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Misalwadi",
    "nameMr": "मिसाळवाडी",
    "pincode": ""
  },
  {
    "id": "172623",
    "code": "172623",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Mohadari",
    "nameMr": "मोहदरी",
    "pincode": ""
  },
  {
    "id": "172624",
    "code": "172624",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Mungsari",
    "nameMr": "मुंगसरी",
    "pincode": ""
  },
  {
    "id": "172625",
    "code": "172625",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Muradpur",
    "nameMr": "मुरादपूर",
    "pincode": ""
  },
  {
    "id": "172626",
    "code": "172626",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Naigaon Bk",
    "nameMr": "नायगाव  बु.",
    "pincode": ""
  },
  {
    "id": "265781",
    "code": "265781",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Naigaon Khurd",
    "nameMr": "नायगाव खुर्द",
    "pincode": ""
  },
  {
    "id": "172627",
    "code": "172627",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Palaskhed Daulat",
    "nameMr": "पळसखेड दौलत",
    "pincode": "443201"
  },
  {
    "id": "172628",
    "code": "172628",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Pandhardeo",
    "nameMr": "पांढरदेव",
    "pincode": ""
  },
  {
    "id": "172629",
    "code": "172629",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Patoda",
    "nameMr": "पाटोदा",
    "pincode": ""
  },
  {
    "id": "172630",
    "code": "172630",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Peth",
    "nameMr": "पेठ",
    "pincode": ""
  },
  {
    "id": "172631",
    "code": "172631",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Pimparkhedpramdapur",
    "nameMr": "पिंपरखेड प्र अमदापूर",
    "pincode": ""
  },
  {
    "id": "172632",
    "code": "172632",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Ranantri",
    "nameMr": "रानअंत्री",
    "pincode": ""
  },
  {
    "id": "172633",
    "code": "172633",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Rohada",
    "nameMr": "रोह्डा",
    "pincode": ""
  },
  {
    "id": "172634",
    "code": "172634",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Sakegaon",
    "nameMr": "साकेगाव",
    "pincode": ""
  },
  {
    "id": "172635",
    "code": "172635",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Satgaon Bhusari",
    "nameMr": "सातगाव भुसारी",
    "pincode": ""
  },
  {
    "id": "172637",
    "code": "172637",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Sawangi Gawali",
    "nameMr": "सावंगी गवळी",
    "pincode": ""
  },
  {
    "id": "172638",
    "code": "172638",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Sawargaondukare",
    "nameMr": "सावरगाव डुकरे",
    "pincode": ""
  },
  {
    "id": "172636",
    "code": "172636",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Sawna",
    "nameMr": "सवना",
    "pincode": ""
  },
  {
    "id": "172639",
    "code": "172639",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Shelgaon Atol",
    "nameMr": "शेलगाव आटोळ",
    "pincode": ""
  },
  {
    "id": "172640",
    "code": "172640",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Shelgaon Jahangir",
    "nameMr": "शेलगाव जहांगीर",
    "pincode": ""
  },
  {
    "id": "172641",
    "code": "172641",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Shelodi",
    "nameMr": "शेलोडी",
    "pincode": ""
  },
  {
    "id": "172642",
    "code": "172642",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Shelsur",
    "nameMr": "शेलसुर",
    "pincode": ""
  },
  {
    "id": "172643",
    "code": "172643",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Shelud",
    "nameMr": "शेलूद",
    "pincode": ""
  },
  {
    "id": "172644",
    "code": "172644",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Shindi Harali",
    "nameMr": "शिंदी हराळी",
    "pincode": ""
  },
  {
    "id": "172645",
    "code": "172645",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Somthana",
    "nameMr": "सोमठाना",
    "pincode": ""
  },
  {
    "id": "172646",
    "code": "172646",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Sonewadi",
    "nameMr": "सोनेवाडी",
    "pincode": ""
  },
  {
    "id": "172647",
    "code": "172647",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Takarkhed Helga",
    "nameMr": "टाकरखेड हेल्गा",
    "pincode": ""
  },
  {
    "id": "172648",
    "code": "172648",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Takarkhedmuslim Pramdapur",
    "nameMr": "ताकारखेद्मुस्लीम प्रअमदापूर",
    "pincode": ""
  },
  {
    "id": "172649",
    "code": "172649",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Telhara",
    "nameMr": "तेल्हारा",
    "pincode": ""
  },
  {
    "id": "172650",
    "code": "172650",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Toranwada",
    "nameMr": "तोरणवाडा",
    "pincode": ""
  },
  {
    "id": "172651",
    "code": "172651",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Undri",
    "nameMr": "उंदरी",
    "pincode": ""
  },
  {
    "id": "172652",
    "code": "172652",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Utrada",
    "nameMr": "उत्रादा",
    "pincode": ""
  },
  {
    "id": "172653",
    "code": "172653",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Vairagad",
    "nameMr": "वैरागड",
    "pincode": ""
  },
  {
    "id": "305352",
    "code": "305352",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Vasantnagar",
    "nameMr": "वसंतनगर",
    "pincode": ""
  },
  {
    "id": "172654",
    "code": "172654",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Walti",
    "nameMr": "वळती",
    "pincode": ""
  },
  {
    "id": "172655",
    "code": "172655",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Warkhed",
    "nameMr": "वरखेड",
    "pincode": ""
  },
  {
    "id": "172656",
    "code": "172656",
    "talukaId": "3983",
    "districtId": "472",
    "nameEn": "Yewata",
    "nameMr": "येवता",
    "pincode": ""
  }
];
export const chikhliPreloadedVillages: Record<string, Village[]> = {
  "172561": [
    {
      "id": "529203",
      "code": "529203",
      "gramPanchayatId": "172561",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Ambashi Bhag 1",
      "nameMr": "अंबाशी भाग 1"
    },
    {
      "id": "944957",
      "code": "944957",
      "gramPanchayatId": "172561",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Ambashi Bhag 2",
      "nameMr": "अंबाशी भाग 2"
    }
  ],
  "172562": [
    {
      "id": "529119",
      "code": "529119",
      "gramPanchayatId": "172562",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Amdapur Bhag 1",
      "nameMr": "अमडापुर भाग 1"
    },
    {
      "id": "944958",
      "code": "944958",
      "gramPanchayatId": "172562",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Amdapur Bhag 2",
      "nameMr": "अमडापूर भाग 2"
    },
    {
      "id": "944959",
      "code": "944959",
      "gramPanchayatId": "172562",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Amdapur Bhag 3",
      "nameMr": "अमडापूर भाग 3"
    }
  ],
  "172563": [
    {
      "id": "529202",
      "code": "529202",
      "gramPanchayatId": "172563",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Amkhed",
      "nameMr": "आमखेड"
    }
  ],
  "172564": [
    {
      "id": "529215",
      "code": "529215",
      "gramPanchayatId": "172564",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Amona",
      "nameMr": "अमोना"
    }
  ],
  "172565": [
    {
      "id": "529220",
      "code": "529220",
      "gramPanchayatId": "172565",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Ancharwadi Bhag 1",
      "nameMr": "अंचरवाडी भाग 1"
    },
    {
      "id": "945053",
      "code": "945053",
      "gramPanchayatId": "172565",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Ancharwadi Bhag 2",
      "nameMr": "अंचरवाडी भाग 2"
    }
  ],
  "172566": [
    {
      "id": "529088",
      "code": "529088",
      "gramPanchayatId": "172566",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Andhai",
      "nameMr": "आंधई"
    },
    {
      "id": "529160",
      "code": "529160",
      "gramPanchayatId": "172566",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Chandhai",
      "nameMr": "चांधई"
    }
  ],
  "172567": [
    {
      "id": "529225",
      "code": "529225",
      "gramPanchayatId": "172567",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Antri Khedekar",
      "nameMr": "अंत्री खेडेकर"
    }
  ],
  "172568": [
    {
      "id": "529185",
      "code": "529185",
      "gramPanchayatId": "172568",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Antri Koli",
      "nameMr": "अंत्री कोळी"
    },
    {
      "id": "529184",
      "code": "529184",
      "gramPanchayatId": "172568",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Waghapur",
      "nameMr": "वाघापूर"
    }
  ],
  "172569": [
    {
      "id": "529223",
      "code": "529223",
      "gramPanchayatId": "172569",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Asola Pr.Kherda",
      "nameMr": "असोला प्र.खेर्डा"
    }
  ],
  "172570": [
    {
      "id": "529198",
      "code": "529198",
      "gramPanchayatId": "172570",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Berala",
      "nameMr": "बेराळा"
    }
  ],
  "172571": [
    {
      "id": "529199",
      "code": "529199",
      "gramPanchayatId": "172571",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Bhalgaon",
      "nameMr": "भालगांव"
    }
  ],
  "172572": [
    {
      "id": "529195",
      "code": "529195",
      "gramPanchayatId": "172572",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Bhankhed.",
      "nameMr": "भानखेड"
    },
    {
      "id": "529196",
      "code": "529196",
      "gramPanchayatId": "172572",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Jambhora",
      "nameMr": "जांभोरा"
    }
  ],
  "172573": [
    {
      "id": "529222",
      "code": "529222",
      "gramPanchayatId": "172573",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Bharosa Bhag 1",
      "nameMr": "भरोसा भाग 1"
    },
    {
      "id": "945054",
      "code": "945054",
      "gramPanchayatId": "172573",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Bharosa Bhag 2",
      "nameMr": "भरोसा भाग 2"
    },
    {
      "id": "529212",
      "code": "529212",
      "gramPanchayatId": "172573",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Ramnagar",
      "nameMr": "रामनगर"
    }
  ],
  "172574": [
    {
      "id": "529189",
      "code": "529189",
      "gramPanchayatId": "172574",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Bhogawati",
      "nameMr": "भोगावती"
    },
    {
      "id": "529190",
      "code": "529190",
      "gramPanchayatId": "172574",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Tambulwadi",
      "nameMr": "तांबुळवाडी"
    }
  ],
  "172575": [
    {
      "id": "529191",
      "code": "529191",
      "gramPanchayatId": "172575",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Bhokar",
      "nameMr": "भोकर"
    }
  ],
  "172576": [
    {
      "id": "529149",
      "code": "529149",
      "gramPanchayatId": "172576",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Bhorsa",
      "nameMr": "भोरसा"
    },
    {
      "id": "529148",
      "code": "529148",
      "gramPanchayatId": "172576",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Bhorsi",
      "nameMr": "भोरसी"
    }
  ],
  "172577": [
    {
      "id": "529141",
      "code": "529141",
      "gramPanchayatId": "172577",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Borgaon Kakade Bhag 1",
      "nameMr": "बोरगाव काकडे भाग 1"
    },
    {
      "id": "944964",
      "code": "944964",
      "gramPanchayatId": "172577",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Borgaon Kakade Bhag 2",
      "nameMr": "बोरगाव काकडे भाग 2"
    }
  ],
  "172578": [
    {
      "id": "529170",
      "code": "529170",
      "gramPanchayatId": "172578",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Borgaon Wasu",
      "nameMr": "बोरगांव वसु"
    }
  ],
  "172579": [
    {
      "id": "529177",
      "code": "529177",
      "gramPanchayatId": "172579",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Bramhapuri",
      "nameMr": "ब्रम्हपुरी"
    },
    {
      "id": "529162",
      "code": "529162",
      "gramPanchayatId": "172579",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Girola",
      "nameMr": "गिरोला"
    },
    {
      "id": "529175",
      "code": "529175",
      "gramPanchayatId": "172579",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Kolari",
      "nameMr": "कोलारी"
    }
  ],
  "172580": [
    {
      "id": "529206",
      "code": "529206",
      "gramPanchayatId": "172580",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Chandanpur",
      "nameMr": "चंदनपूर"
    }
  ],
  "172581": [
    {
      "id": "529193",
      "code": "529193",
      "gramPanchayatId": "172581",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Chandhai Pr.Chikhali",
      "nameMr": "चांधई प्र.चिखली"
    },
    {
      "id": "529194",
      "code": "529194",
      "gramPanchayatId": "172581",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Pambulwadi",
      "nameMr": "पांबुळवाडी"
    }
  ],
  "172582": [
    {
      "id": "529137",
      "code": "529137",
      "gramPanchayatId": "172582",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Dahigaon",
      "nameMr": "दहिगाव"
    }
  ],
  "172583": [
    {
      "id": "529095",
      "code": "529095",
      "gramPanchayatId": "172583",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Dasala",
      "nameMr": "डासाळा"
    }
  ],
  "172584": [
    {
      "id": "529213",
      "code": "529213",
      "gramPanchayatId": "172584",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Deulgaon Dhangar",
      "nameMr": "देऊळगांव धनगर"
    }
  ],
  "172585": [
    {
      "id": "529131",
      "code": "529131",
      "gramPanchayatId": "172585",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Dhanori",
      "nameMr": "धानोरी"
    },
    {
      "id": "529132",
      "code": "529132",
      "gramPanchayatId": "172585",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Mungi",
      "nameMr": "मुंगी"
    },
    {
      "id": "529128",
      "code": "529128",
      "gramPanchayatId": "172585",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Sawarkhed Bk.",
      "nameMr": "सावरखेड बुद्रुक"
    },
    {
      "id": "529129",
      "code": "529129",
      "gramPanchayatId": "172585",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Sawarkhed Kh.",
      "nameMr": "सावरखेड खुर्द"
    }
  ],
  "172586": [
    {
      "id": "529159",
      "code": "529159",
      "gramPanchayatId": "172586",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Dhodap",
      "nameMr": "धोडप"
    },
    {
      "id": "529158",
      "code": "529158",
      "gramPanchayatId": "172586",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Palaskhed Sapkal",
      "nameMr": "पळसखेड सपकाळ"
    }
  ],
  "172587": [
    {
      "id": "529135",
      "code": "529135",
      "gramPanchayatId": "172587",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Dhotra Bhangoji Bhag 1",
      "nameMr": "धोत्रा भनगोजी भाग 1"
    },
    {
      "id": "945056",
      "code": "945056",
      "gramPanchayatId": "172587",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Dhotra Bhangoji Bhag 2",
      "nameMr": "धोत्रा भनगोजी भाग 2"
    }
  ],
  "172588": [
    {
      "id": "529107",
      "code": "529107",
      "gramPanchayatId": "172588",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Dhotra Naik",
      "nameMr": "धोत्रा नाईक"
    },
    {
      "id": "529108",
      "code": "529108",
      "gramPanchayatId": "172588",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Hiwara Naik",
      "nameMr": "हिवरा नाईक"
    }
  ],
  "172589": [
    {
      "id": "529168",
      "code": "529168",
      "gramPanchayatId": "172589",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Beldari",
      "nameMr": "बेलदरी"
    },
    {
      "id": "529167",
      "code": "529167",
      "gramPanchayatId": "172589",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Diwathana",
      "nameMr": "दिवठाणा"
    }
  ],
  "172590": [
    {
      "id": "529087",
      "code": "529087",
      "gramPanchayatId": "172590",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Borala Pra. Amdapur",
      "nameMr": "बोराळा प्र. अमडापुर"
    },
    {
      "id": "529086",
      "code": "529086",
      "gramPanchayatId": "172590",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Dongar Shewali",
      "nameMr": "डोंगर शेवली"
    }
  ],
  "172591": [
    {
      "id": "529155",
      "code": "529155",
      "gramPanchayatId": "172591",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Dongargaon",
      "nameMr": "डोंगरगाव"
    },
    {
      "id": "529156",
      "code": "529156",
      "gramPanchayatId": "172591",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Kotkhed",
      "nameMr": "कोटखेड"
    }
  ],
  "172592": [
    {
      "id": "529146",
      "code": "529146",
      "gramPanchayatId": "172592",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Eklara",
      "nameMr": "एकलारा"
    }
  ],
  "172593": [
    {
      "id": "529204",
      "code": "529204",
      "gramPanchayatId": "172593",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Gangalgaon",
      "nameMr": "गांगलगाव"
    }
  ],
  "172594": [
    {
      "id": "529174",
      "code": "529174",
      "gramPanchayatId": "172594",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Godri",
      "nameMr": "गोद्री"
    }
  ],
  "172678": [
    {
      "id": "529227",
      "code": "529227",
      "gramPanchayatId": "172678",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Gunjala",
      "nameMr": "गुंजाळा"
    },
    {
      "id": "529228",
      "code": "529228",
      "gramPanchayatId": "172678",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Pimpalwadi",
      "nameMr": "पिंपलवाडी"
    }
  ],
  "172595": [
    {
      "id": "529092",
      "code": "529092",
      "gramPanchayatId": "172595",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Harni",
      "nameMr": "हरणी"
    }
  ],
  "172596": [
    {
      "id": "529181",
      "code": "529181",
      "gramPanchayatId": "172596",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Hatni",
      "nameMr": "हातणी"
    }
  ],
  "172597": [
    {
      "id": "529130",
      "code": "529130",
      "gramPanchayatId": "172597",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Isoli Bhag 1",
      "nameMr": "इसोली भाग 1"
    },
    {
      "id": "944960",
      "code": "944960",
      "gramPanchayatId": "172597",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Isoli Bhag 2",
      "nameMr": "इसोली भाग 2"
    }
  ],
  "172598": [
    {
      "id": "529217",
      "code": "529217",
      "gramPanchayatId": "172598",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Isrul",
      "nameMr": "इसरुळ"
    }
  ],
  "172599": [
    {
      "id": "529111",
      "code": "529111",
      "gramPanchayatId": "172599",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Gondhankhed",
      "nameMr": "गोधंणखेड"
    },
    {
      "id": "529134",
      "code": "529134",
      "gramPanchayatId": "172599",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Karankhed",
      "nameMr": "करणखेड"
    },
    {
      "id": "529110",
      "code": "529110",
      "gramPanchayatId": "172599",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Khamkhed",
      "nameMr": "खामखेड"
    },
    {
      "id": "529112",
      "code": "529112",
      "gramPanchayatId": "172599",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Mahattamkhed",
      "nameMr": "महात्तमखेड"
    }
  ],
  "172600": [
    {
      "id": "529117",
      "code": "529117",
      "gramPanchayatId": "172600",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Ghanmod",
      "nameMr": "घनमोड"
    },
    {
      "id": "529116",
      "code": "529116",
      "gramPanchayatId": "172600",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Karatwadi",
      "nameMr": "करतवाडी"
    },
    {
      "id": "529138",
      "code": "529138",
      "gramPanchayatId": "172600",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Manmod",
      "nameMr": "मानमोड"
    }
  ],
  "172601": [
    {
      "id": "529157",
      "code": "529157",
      "gramPanchayatId": "172601",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Belkhed",
      "nameMr": "बेलखेड"
    },
    {
      "id": "529090",
      "code": "529090",
      "gramPanchayatId": "172601",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Karwand Bhag 1",
      "nameMr": "करवंड भाग 1"
    },
    {
      "id": "945057",
      "code": "945057",
      "gramPanchayatId": "172601",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Karwand Bhag 2",
      "nameMr": "करवंड भाग 2"
    }
  ],
  "172602": [
    {
      "id": "529207",
      "code": "529207",
      "gramPanchayatId": "172602",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Katoda",
      "nameMr": "काटोडा"
    }
  ],
  "172603": [
    {
      "id": "529094",
      "code": "529094",
      "gramPanchayatId": "172603",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Kavhala",
      "nameMr": "कव्हळा"
    },
    {
      "id": "529113",
      "code": "529113",
      "gramPanchayatId": "172603",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Sawarkhed Najik",
      "nameMr": "सावरखेड नजीक"
    }
  ],
  "172604": [
    {
      "id": "529205",
      "code": "529205",
      "gramPanchayatId": "172604",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Kawathal",
      "nameMr": "कवठळ"
    }
  ],
  "172605": [
    {
      "id": "529178",
      "code": "529178",
      "gramPanchayatId": "172605",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Kelwad",
      "nameMr": "केळवद"
    }
  ],
  "172606": [
    {
      "id": "529201",
      "code": "529201",
      "gramPanchayatId": "172606",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Khairav",
      "nameMr": "खैरव"
    }
  ],
  "172607": [
    {
      "id": "529172",
      "code": "529172",
      "gramPanchayatId": "172607",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Khandala Makardhwaj",
      "nameMr": "खंडाळा मकरध्वज"
    }
  ],
  "172608": [
    {
      "id": "529173",
      "code": "529173",
      "gramPanchayatId": "172608",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Khor",
      "nameMr": "खोर"
    }
  ],
  "172609": [
    {
      "id": "529109",
      "code": "529109",
      "gramPanchayatId": "172609",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Kinhi Naik",
      "nameMr": "किन्ही नाईक"
    }
  ],
  "172610": [
    {
      "id": "529098",
      "code": "529098",
      "gramPanchayatId": "172610",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Kinhi Sawadad",
      "nameMr": "किन्ही सवडद"
    },
    {
      "id": "529099",
      "code": "529099",
      "gramPanchayatId": "172610",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Kusumba",
      "nameMr": "कुसूंबा"
    },
    {
      "id": "529100",
      "code": "529100",
      "gramPanchayatId": "172610",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Kusumbi",
      "nameMr": "कुसूंबी"
    }
  ],
  "172611": [
    {
      "id": "529176",
      "code": "529176",
      "gramPanchayatId": "172611",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Kinhola",
      "nameMr": "किन्होळा"
    }
  ],
  "172612": [
    {
      "id": "529200",
      "code": "529200",
      "gramPanchayatId": "172612",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Kolara Bhag 1",
      "nameMr": "कोलारा भाग 1"
    },
    {
      "id": "944961",
      "code": "944961",
      "gramPanchayatId": "172612",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Kolara Bhag 2",
      "nameMr": "कोलारा भाग 2"
    }
  ],
  "172613": [
    {
      "id": "529214",
      "code": "529214",
      "gramPanchayatId": "172613",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Konad",
      "nameMr": "कोनड"
    }
  ],
  "172614": [
    {
      "id": "529115",
      "code": "529115",
      "gramPanchayatId": "172614",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Mahimal",
      "nameMr": "महीमळ"
    }
  ],
  "172615": [
    {
      "id": "529182",
      "code": "529182",
      "gramPanchayatId": "172615",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Malgani",
      "nameMr": "माळगनी"
    }
  ],
  "172616": [
    {
      "id": "529209",
      "code": "529209",
      "gramPanchayatId": "172616",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Malagi",
      "nameMr": "मलगी"
    }
  ],
  "172617": [
    {
      "id": "529187",
      "code": "529187",
      "gramPanchayatId": "172617",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Borala",
      "nameMr": "बोराळा"
    },
    {
      "id": "529186",
      "code": "529186",
      "gramPanchayatId": "172617",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Malshemba",
      "nameMr": "मालशेंबा"
    }
  ],
  "172619": [
    {
      "id": "529124",
      "code": "529124",
      "gramPanchayatId": "172619",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Babhulgaon",
      "nameMr": "बाभुळगांव"
    },
    {
      "id": "529125",
      "code": "529125",
      "gramPanchayatId": "172619",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Mangrur Nawghare",
      "nameMr": "मंगरुळ नवघरे"
    }
  ],
  "172618": [
    {
      "id": "529216",
      "code": "529216",
      "gramPanchayatId": "172618",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Mangrul Pr Kherda",
      "nameMr": "मंगरुळ प्र.खेर्डा"
    }
  ],
  "172682": [
    {
      "id": "529229",
      "code": "529229",
      "gramPanchayatId": "172682",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Manubai",
      "nameMr": "मनुबाई"
    }
  ],
  "172620": [
    {
      "id": "529226",
      "code": "529226",
      "gramPanchayatId": "172620",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Mera Bk. Bhag 1",
      "nameMr": "मेरा बु. भाग 1"
    },
    {
      "id": "945174",
      "code": "945174",
      "gramPanchayatId": "172620",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Mera Bk.Bhag 2",
      "nameMr": "मेरा बु. भाग 2"
    }
  ],
  "172621": [
    {
      "id": "529224",
      "code": "529224",
      "gramPanchayatId": "172621",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Mera Kh.",
      "nameMr": "मेरा खुर्द"
    }
  ],
  "172622": [
    {
      "id": "529219",
      "code": "529219",
      "gramPanchayatId": "172622",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Misalwadi Bhag 1",
      "nameMr": "मिसाळवाडी भाग 1"
    },
    {
      "id": "944962",
      "code": "944962",
      "gramPanchayatId": "172622",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Misalwali Bhag 2",
      "nameMr": "मिसाळवाडी भाग 2"
    }
  ],
  "172623": [
    {
      "id": "529105",
      "code": "529105",
      "gramPanchayatId": "172623",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Asola Naik",
      "nameMr": "असोला नाईक"
    },
    {
      "id": "529104",
      "code": "529104",
      "gramPanchayatId": "172623",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Medsing",
      "nameMr": "मेडसींग"
    },
    {
      "id": "529103",
      "code": "529103",
      "gramPanchayatId": "172623",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Mohadari",
      "nameMr": "मोहदरी"
    },
    {
      "id": "529106",
      "code": "529106",
      "gramPanchayatId": "172623",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Vardada",
      "nameMr": "वरदडा"
    }
  ],
  "172624": [
    {
      "id": "529145",
      "code": "529145",
      "gramPanchayatId": "172624",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Mungasari",
      "nameMr": "मुंगसरी"
    }
  ],
  "172625": [
    {
      "id": "529210",
      "code": "529210",
      "gramPanchayatId": "172625",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Muradpur",
      "nameMr": "मुरादपूर"
    }
  ],
  "172626": [
    {
      "id": "529153",
      "code": "529153",
      "gramPanchayatId": "172626",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Naigaon Bk.",
      "nameMr": "नाईगाव बुद्रुक"
    }
  ],
  "265781": [
    {
      "id": "529154",
      "code": "529154",
      "gramPanchayatId": "265781",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Naigaon Kh.",
      "nameMr": "नाईगाव खुर्द"
    }
  ],
  "172627": [
    {
      "id": "529192",
      "code": "529192",
      "gramPanchayatId": "172627",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Palaskhed Daulat",
      "nameMr": "पळसखेड दौलत"
    }
  ],
  "172628": [
    {
      "id": "529151",
      "code": "529151",
      "gramPanchayatId": "172628",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Dewadari",
      "nameMr": "देवदरी"
    },
    {
      "id": "529150",
      "code": "529150",
      "gramPanchayatId": "172628",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Jambhrun",
      "nameMr": "जांभरुन"
    },
    {
      "id": "529140",
      "code": "529140",
      "gramPanchayatId": "172628",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Pandhardeo",
      "nameMr": "पांढरदेव"
    }
  ],
  "172629": [
    {
      "id": "529147",
      "code": "529147",
      "gramPanchayatId": "172629",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Patoda",
      "nameMr": "पाटोदा"
    }
  ],
  "172630": [
    {
      "id": "529169",
      "code": "529169",
      "gramPanchayatId": "172630",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Peth",
      "nameMr": "पेठ"
    }
  ],
  "172631": [
    {
      "id": "529120",
      "code": "529120",
      "gramPanchayatId": "172631",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Dhuma",
      "nameMr": "ढुमा"
    },
    {
      "id": "529121",
      "code": "529121",
      "gramPanchayatId": "172631",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Haralkhed",
      "nameMr": "हराळखेड"
    },
    {
      "id": "529122",
      "code": "529122",
      "gramPanchayatId": "172631",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Karkheda",
      "nameMr": "कारखेड"
    },
    {
      "id": "529123",
      "code": "529123",
      "gramPanchayatId": "172631",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Pimparkhed",
      "nameMr": "पिंपरखेड"
    }
  ],
  "172632": [
    {
      "id": "529208",
      "code": "529208",
      "gramPanchayatId": "172632",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Ranantri",
      "nameMr": "रानअंत्री"
    }
  ],
  "172633": [
    {
      "id": "529211",
      "code": "529211",
      "gramPanchayatId": "172633",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Rohada",
      "nameMr": "रोहडा"
    }
  ],
  "172634": [
    {
      "id": "529188",
      "code": "529188",
      "gramPanchayatId": "172634",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Sakegaon",
      "nameMr": "साकेगाव"
    }
  ],
  "172635": [
    {
      "id": "529180",
      "code": "529180",
      "gramPanchayatId": "172635",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Satgaon Bhusari",
      "nameMr": "सातगांव भुसारी"
    }
  ],
  "172637": [
    {
      "id": "529152",
      "code": "529152",
      "gramPanchayatId": "172637",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Sawangi Gawali Bhag 1",
      "nameMr": "सावंगी गवळी भाग 1"
    },
    {
      "id": "944965",
      "code": "944965",
      "gramPanchayatId": "172637",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Sawangi Gawali Bhag 2",
      "nameMr": "सावंगी गवळी भाग 2"
    }
  ],
  "172638": [
    {
      "id": "529183",
      "code": "529183",
      "gramPanchayatId": "172638",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Sawargaon Dukare",
      "nameMr": "सावरगांव डुकरे"
    }
  ],
  "172636": [
    {
      "id": "944963",
      "code": "944963",
      "gramPanchayatId": "172636",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Sawana Bhag 2",
      "nameMr": "सवणा भाग 2"
    },
    {
      "id": "529161",
      "code": "529161",
      "gramPanchayatId": "172636",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Sawna Bhag 1",
      "nameMr": "सवणा भाग 1"
    }
  ],
  "172639": [
    {
      "id": "529218",
      "code": "529218",
      "gramPanchayatId": "172639",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Shelgaon Atol",
      "nameMr": "शेलगाव  अटोल"
    }
  ],
  "172640": [
    {
      "id": "529143",
      "code": "529143",
      "gramPanchayatId": "172640",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Anwi",
      "nameMr": "आन्वी"
    },
    {
      "id": "529144",
      "code": "529144",
      "gramPanchayatId": "172640",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Shelgaon Jahangir",
      "nameMr": "शेलगाव जहांगीर"
    }
  ],
  "172641": [
    {
      "id": "529133",
      "code": "529133",
      "gramPanchayatId": "172641",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Shelodi",
      "nameMr": "शेलोडी"
    }
  ],
  "172642": [
    {
      "id": "529089",
      "code": "529089",
      "gramPanchayatId": "172642",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Shelsur",
      "nameMr": "शेलसुर"
    }
  ],
  "172643": [
    {
      "id": "529164",
      "code": "529164",
      "gramPanchayatId": "172643",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Palaskhed Jayanti",
      "nameMr": "पळसखेड जयंती"
    },
    {
      "id": "529165",
      "code": "529165",
      "gramPanchayatId": "172643",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Shelud",
      "nameMr": "शेलूद"
    }
  ],
  "172644": [
    {
      "id": "529171",
      "code": "529171",
      "gramPanchayatId": "172644",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Shindi Harali",
      "nameMr": "शिंदी हराळी"
    }
  ],
  "172645": [
    {
      "id": "529166",
      "code": "529166",
      "gramPanchayatId": "172645",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Somthana",
      "nameMr": "सोमठाणा"
    }
  ],
  "172646": [
    {
      "id": "529179",
      "code": "529179",
      "gramPanchayatId": "172646",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Sonewadi",
      "nameMr": "सोनेवाडी"
    }
  ],
  "172647": [
    {
      "id": "529093",
      "code": "529093",
      "gramPanchayatId": "172647",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Takarkhed Helga",
      "nameMr": "टाकरखेड हेल्गा"
    }
  ],
  "172648": [
    {
      "id": "529118",
      "code": "529118",
      "gramPanchayatId": "172648",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Ainkhed",
      "nameMr": "ऐनखेड"
    },
    {
      "id": "529114",
      "code": "529114",
      "gramPanchayatId": "172648",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Takarkhed Musalman",
      "nameMr": "टाकरखेड मुसलमान"
    }
  ],
  "172649": [
    {
      "id": "529142",
      "code": "529142",
      "gramPanchayatId": "172649",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Telhara",
      "nameMr": "तेल्हारा"
    }
  ],
  "172650": [
    {
      "id": "529102",
      "code": "529102",
      "gramPanchayatId": "172650",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Jamdara",
      "nameMr": "जामदरा"
    },
    {
      "id": "529101",
      "code": "529101",
      "gramPanchayatId": "172650",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Toranwada",
      "nameMr": "तोरणवाडा"
    }
  ],
  "172651": [
    {
      "id": "529096",
      "code": "529096",
      "gramPanchayatId": "172651",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Udaynagar",
      "nameMr": "उदयनगर"
    }
  ],
  "172652": [
    {
      "id": "529136",
      "code": "529136",
      "gramPanchayatId": "172652",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Utrada",
      "nameMr": "उत्रादा"
    }
  ],
  "172653": [
    {
      "id": "529097",
      "code": "529097",
      "gramPanchayatId": "172653",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Vairagad",
      "nameMr": "वैरागड"
    }
  ],
  "305352": [
    {
      "id": "529221",
      "code": "529221",
      "gramPanchayatId": "305352",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Vasantnagar",
      "nameMr": "वसंतनगर"
    }
  ],
  "172654": [
    {
      "id": "529163",
      "code": "529163",
      "gramPanchayatId": "172654",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Walti",
      "nameMr": "वळती"
    }
  ],
  "172655": [
    {
      "id": "529139",
      "code": "529139",
      "gramPanchayatId": "172655",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Borakhedi",
      "nameMr": "बोराखेडी"
    },
    {
      "id": "529127",
      "code": "529127",
      "gramPanchayatId": "172655",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Gondhankhed Pr. Amdapur",
      "nameMr": "गोधंणखेड प्र. अमडापूर"
    },
    {
      "id": "529126",
      "code": "529126",
      "gramPanchayatId": "172655",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Warkhed",
      "nameMr": "वरखेड"
    }
  ],
  "172656": [
    {
      "id": "529197",
      "code": "529197",
      "gramPanchayatId": "172656",
      "talukaId": "3983",
      "districtId": "472",
      "nameEn": "Yewata",
      "nameMr": "येवता"
    }
  ]
};

// In-memory cache for all 358 talukas (loaded on demand from /data/lgd/talukas/<talukaId>.json)
const talukaCache: Record<string, { gramPanchayats: GramPanchayat[]; villages: Record<string, Village[]> }> = {
  '3983': {
    gramPanchayats: chikhliPreloadedGps,
    villages: chikhliPreloadedVillages
  }
};

// Global GP lookup index for O(1) retrieval
const gpIndex: Map<string, GramPanchayat> = new Map();
chikhliPreloadedGps.forEach((gp) => gpIndex.set(gp.id, gp));

// Global Village lookup index for O(1) retrieval
const villageIndex: Map<string, Village> = new Map();
Object.values(chikhliPreloadedVillages).flat().forEach((v) => villageIndex.set(v.id, v));

export const getDistricts = (): District[] => districts;

export const getDistrictById = (districtId: string): District | undefined => {
  if (!districtId) return undefined;
  return districts.find((d) => d.id === districtId || d.code === districtId || d.nameEn.toLowerCase() === districtId.toLowerCase());
};

export const getTalukasByDistrict = (districtId: string): Taluka[] => {
  if (!districtId) return [];
  // Match either by LGD district code or legacy ID
  const dist = getDistrictById(districtId);
  const targetCode = dist ? dist.code : districtId;
  return talukas.filter((t) => t.districtId === targetCode);
};

export const getTalukaById = (talukaId: string): Taluka | undefined => {
  if (!talukaId) return undefined;
  return talukas.find((t) => t.id === talukaId || t.code === talukaId || t.nameEn.toLowerCase() === talukaId.toLowerCase());
};

/**
 * Loads the complete official Gram Panchayats and Mapped Villages for a specific Taluka.
 * If already cached or preloaded, returns immediately.
 * Otherwise fetches the optimized JSON file `/data/lgd/talukas/<talukaId>.json`.
 */
export async function loadTalukaLgdData(
  talukaId: string
): Promise<{ gramPanchayats: GramPanchayat[]; villages: Record<string, Village[]> }> {
  if (!talukaId) return { gramPanchayats: [], villages: {} };

  if (talukaCache[talukaId]) {
    return talukaCache[talukaId];
  }

  try {
    const response = await fetch(`/data/lgd/talukas/${talukaId}.json`);
    if (!response.ok) {
      console.warn(`Could not load LGD data for taluka ${talukaId}: status ${response.status}`);
      return { gramPanchayats: [], villages: {} };
    }
    const data = await response.json();
    const gps: GramPanchayat[] = data.gramPanchayats || [];
    const villages: Record<string, Village[]> = data.villages || {};

    // Index them in memory
    gps.forEach((gp) => gpIndex.set(gp.id, gp));
    Object.values(villages).flat().forEach((v) => villageIndex.set(v.id, v));

    talukaCache[talukaId] = {
      gramPanchayats: gps,
      villages: villages
    };

    return talukaCache[talukaId];
  } catch (err) {
    console.error(`Error loading LGD data for taluka ${talukaId}:`, err);
    return { gramPanchayats: [], villages: {} };
  }
}

/**
 * Synchronous Gram Panchayat getter (returns from cache or preloaded Chikhli)
 */
export const getGramPanchayatsByTaluka = (talukaId: string): GramPanchayat[] => {
  if (!talukaId) return [];
  if (talukaCache[talukaId]) {
    return talukaCache[talukaId].gramPanchayats;
  }
  // Trigger background fetch if not yet loaded
  loadTalukaLgdData(talukaId).catch(() => {});
  return [];
};

/**
 * Synchronous Village getter for a Gram Panchayat
 */
export const getVillagesByGramPanchayat = (gpId: string): Village[] => {
  if (!gpId) return [];
  for (const tId in talukaCache) {
    const vMap = talukaCache[tId].villages;
    if (vMap && vMap[gpId]) {
      return vMap[gpId];
    }
  }
  return [];
};

export const getGramPanchayatById = (gpId: string): GramPanchayat | undefined => {
  if (!gpId) return undefined;
  if (gpIndex.has(gpId)) return gpIndex.get(gpId);

  // Search in all cached talukas
  for (const tId in talukaCache) {
    const found = talukaCache[tId].gramPanchayats.find((g) => g.id === gpId || g.code === gpId);
    if (found) {
      gpIndex.set(gpId, found);
      return found;
    }
  }
  return undefined;
};

export const getVillageById = (villageId: string): Village | undefined => {
  if (!villageId) return undefined;
  if (villageIndex.has(villageId)) return villageIndex.get(villageId);

  for (const tId in talukaCache) {
    const vMap = talukaCache[tId].villages;
    for (const gpKey in vMap) {
      const found = vMap[gpKey].find((v) => v.id === villageId || v.code === villageId);
      if (found) {
        villageIndex.set(villageId, found);
        return found;
      }
    }
  }
  return undefined;
};
