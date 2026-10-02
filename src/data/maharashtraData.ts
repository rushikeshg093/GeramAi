import {
  District,
  Taluka,
  GramPanchayat,
  Village,
  PreapprovedOfficer,
  PanchayatProfile,
  Official,
  Project,
  OnlineServiceItem,
  Notice,
  WaterSchedule,
  Complaint
} from '../types';

export {
  districts,
  talukas,
  getDistricts,
  getDistrictById,
  getTalukaById,
  getTalukasByDistrict,
  getGramPanchayatsByTaluka,
  getVillagesByGramPanchayat,
  getGramPanchayatById,
  getVillageById,
  loadTalukaLgdData,
  lgdSummary
} from './lgdMaster';

import {
  districts as officialDistricts,
  talukas as officialTalukas,
  chikhliPreloadedGps,
  chikhliPreloadedVillages,
  getDistrictById as lgdGetDistrictById,
  getTalukaById as lgdGetTalukaById,
  getGramPanchayatById as lgdGetGramPanchayatById,
  getTalukasByDistrict as lgdGetTalukasByDistrict
} from './lgdMaster';

// Alias dictionary for backward compatibility with previous sample/demo IDs
const ID_ALIASES: Record<string, string> = {
  'buldhana': '472',
  '472': '472',
  'chikhli': '3983',
  '3983': '3983',
  'gp_palaskhed_daulat': '172627',
  '172627': '172627',
  'pune': '490',
  '490': '490',
  'haveli': '4193',
  '4193': '4193',
  'kolhapur': '480',
  '480': '480',
  'karvir': '4147',
  '4147': '4147'
};

export const normalizeLgdId = (id: string): string => {
  if (!id) return id;
  return ID_ALIASES[id.toLowerCase()] || id;
};

// Export gramPanchayats array for pre-loaded synchronous access (Chikhli 100 GPs)
export const gramPanchayats: GramPanchayat[] = chikhliPreloadedGps;

// ==========================================
// PRE-APPROVED OFFICERS (Mapped to official LGD codes)
// Buldhana (472) -> Chikhli (3983) -> Palaskhed Daulat (172627)
// ==========================================
export const preapprovedOfficers: PreapprovedOfficer[] = [
  {
    adminId: 'OFF-BULD-CHK-001',
    mobileNumber: '9423889900',
    officialEmail: 'gramsevak.palaskhed@mahagp.gov.in',
    fullName: 'श्री. गणेश बाळकृष्ण जोशी',
    fullNameEn: 'Mr. Ganesh Balkrishna Joshi',
    designation: 'ग्रामविकास अधिकारी / ग्रामसेवक (VDO)',
    designationEn: 'Village Development Officer (Gramsevak)',
    districtId: '472',
    talukaId: '3983',
    gramPanchayatId: '172627',
    gramPanchayatNameMr: 'पळसखेड दौलत',
    gramPanchayatNameEn: 'Palaskhed Daulat',
    activationToken: 'ACT-7890',
    defaultOtp: '852963',
    status: 'ACTIVE',
    active: true,
    role: 'admin'
  },
  {
    adminId: 'OFF-BULD-CHK-002',
    mobileNumber: '9822114455',
    officialEmail: 'sarpanch.palaskhed@mahagp.gov.in',
    fullName: 'सौ. सुजाता आनंदराव पाटील',
    fullNameEn: 'Mrs. Sujata Anandrao Patil',
    designation: 'सरपंच (Village Head)',
    designationEn: 'Sarpanch',
    districtId: '472',
    talukaId: '3983',
    gramPanchayatId: '172627',
    gramPanchayatNameMr: 'पळसखेड दौलत',
    gramPanchayatNameEn: 'Palaskhed Daulat',
    activationToken: 'ACT-1234',
    defaultOtp: '852963',
    status: 'ACTIVE',
    active: true,
    role: 'officer'
  },
  {
    adminId: 'OFF-BULD-CHK-003',
    mobileNumber: '9881234567',
    officialEmail: 'upsarpanch.palaskhed@mahagp.gov.in',
    fullName: 'श्री. दीपक वसंतराव देशमुख',
    fullNameEn: 'Mr. Deepak Vasantrao Deshmukh',
    designation: 'उपसरपंच (Deputy Head)',
    designationEn: 'Deputy Sarpanch',
    districtId: '472',
    talukaId: '3983',
    gramPanchayatId: '172627',
    gramPanchayatNameMr: 'पळसखेड दौलत',
    gramPanchayatNameEn: 'Palaskhed Daulat',
    activationToken: 'ACT-5678',
    defaultOtp: '852963',
    status: 'ACTIVE',
    active: true,
    role: 'officer'
  },
  {
    adminId: 'OFF-BULD-CHK-004',
    mobileNumber: '9422998877',
    officialEmail: 'clerk.palaskhed@mahagp.gov.in',
    fullName: 'श्री. मारुती तुकाराम जाधव',
    fullNameEn: 'Mr. Maruti Tukaram Jadhav',
    designation: 'संगणक परिचालक / डेटा ऑपरेटर (Data Operator)',
    designationEn: 'Computer Operator',
    districtId: '472',
    talukaId: '3983',
    gramPanchayatId: '172627',
    gramPanchayatNameMr: 'पळसखेड दौलत',
    gramPanchayatNameEn: 'Palaskhed Daulat',
    activationToken: 'ACT-9012',
    defaultOtp: '852963',
    status: 'ACTIVE',
    active: true,
    role: 'officer'
  },
  {
    adminId: 'OFF-PUNE-HAV-001',
    mobileNumber: '9823012345',
    officialEmail: 'vdo.wagholi@mahagp.gov.in',
    fullName: 'श्री. सचिन मारुती शिंदे',
    fullNameEn: 'Mr. Sachin Maruti Shinde',
    designation: 'ग्रामविकास अधिकारी (VDO)',
    designationEn: 'Village Development Officer',
    districtId: '490',
    talukaId: '4193',
    gramPanchayatId: '186780',
    gramPanchayatNameMr: 'वाघोली',
    gramPanchayatNameEn: 'Wagholi',
    activationToken: 'ACT-WAGH',
    defaultOtp: '852963',
    status: 'ACTIVE',
    active: true,
    role: 'admin'
  }
];

// Officials (Sarpanch, Gramsevak, etc.)
export const defaultOfficials: Official[] = [
  {
    id: '1',
    name: 'सौ. सुजाता आनंदराव पाटील',
    roleMr: 'सरपंच',
    roleEn: 'Sarpanch',
    phone: '९८२२११४४५५'
  },
  {
    id: '2',
    name: 'श्री. दीपक वसंतराव देशमुख',
    roleMr: 'उपसरपंच',
    roleEn: 'Deputy Sarpanch',
    phone: '९८८१२३४५६७'
  },
  {
    id: '3',
    name: 'श्री. गणेश बाळकृष्ण जोशी',
    roleMr: 'ग्रामविकास अधिकारी / ग्रामसेवक',
    roleEn: 'Village Development Officer (Gramsevak)',
    phone: '९४२३८८९९००'
  },
  {
    id: '4',
    name: 'श्री. मारुती तुकाराम जाधव',
    roleMr: 'संगणक परिचालक',
    roleEn: 'Computer Operator',
    phone: '९४२२९९८८७७'
  }
];

// Online Services
export const defaultOnlineServices: OnlineServiceItem[] = [
  {
    id: 'srv-1',
    code: 'SRV-01',
    titleMr: 'जन्म प्रमाणपत्र (Birth Certificate)',
    titleEn: 'Birth Certificate',
    departmentMr: 'आरोग्य व जन्म-मृत्यू विभाग',
    departmentEn: 'Health & Registration Dept',
    descriptionMr: 'नवजात बालकाच्या जन्माचा अधिकृत दाखला मिळवा.',
    descriptionEn: 'Official certificate for birth registration.',
    fee: 20,
    deliveryDays: 3,
    requiredDocuments: ['अस्पताल डिस्चार्ज कार्ड', 'आई-वडिलांचे आधार कार्ड']
  },
  {
    id: 'srv-2',
    code: 'SRV-02',
    titleMr: 'मृत्यू प्रमाणपत्र (Death Certificate)',
    titleEn: 'Death Certificate',
    departmentMr: 'आरोग्य व जन्म-मृत्यू विभाग',
    departmentEn: 'Health & Registration Dept',
    descriptionMr: 'मृत्यूची नोंद व अधिकृत दाखला मिळवा.',
    descriptionEn: 'Official certificate for death registration.',
    fee: 20,
    deliveryDays: 3,
    requiredDocuments: ['डॉक्टर वैद्यकीय अहवाल', 'स्मशानभूमी पावती', 'आधार कार्ड']
  },
  {
    id: 'srv-3',
    code: 'SRV-03',
    titleMr: 'रहिवासी दाखला (Residence Certificate)',
    titleEn: 'Residence Certificate',
    departmentMr: 'सामान्य प्रशासन विभाग',
    departmentEn: 'General Administration Dept',
    descriptionMr: 'ग्रामपंचायत क्षेत्रातील रहिवासाचा अधिकृत पुरावा.',
    descriptionEn: 'Official proof of residence in the village.',
    fee: 10,
    deliveryDays: 1,
    requiredDocuments: ['रेशन कार्ड', 'वीज बिल / आधार कार्ड']
  },
  {
    id: 'srv-4',
    code: 'SRV-04',
    titleMr: 'घरपट्टी व पाणीपट्टी कर आकारणी (Property & Water Tax)',
    titleEn: 'Property & Water Tax Assessment',
    departmentMr: 'कर आकारणी विभाग',
    departmentEn: 'Tax Assessment Dept',
    descriptionMr: 'वार्षिक घर कर आणि पाणी कर पावती मिळवा किंवा ऑनलाइन भरा.',
    descriptionEn: 'Pay property and water utility tax online.',
    fee: 0,
    deliveryDays: 1,
    requiredDocuments: ['मागील वर्षाची कर पावती / मालमत्ता क्रमांक']
  },
  {
    id: 'srv-5',
    code: 'SRV-05',
    titleMr: 'पाणीपुरवठा जोडणी अर्ज (Water Connection)',
    titleEn: 'New Water Connection Application',
    departmentMr: 'पाणीपुरवठा विभाग',
    departmentEn: 'Water Supply Dept',
    descriptionMr: 'नवीन घरगुती पाणीपुरवठा नळ जोडणीसाठी अर्ज करा.',
    descriptionEn: 'Apply for a new household drinking water connection.',
    fee: 500,
    deliveryDays: 7,
    requiredDocuments: ['घरपट्टी पावती', 'आधार कार्ड', 'जागेचा नकाशा']
  },
  {
    id: 'srv-6',
    code: 'SRV-06',
    titleMr: 'बांधकाम परवानगी (Building Permission / NOC)',
    titleEn: 'Construction NOC',
    departmentMr: 'नगररचना व बांधकाम विभाग',
    departmentEn: 'Town Planning & Building Dept',
    descriptionMr: 'नवीन घर किंवा दुकानासाठी ग्रामपंचायत ना-हरकत दाखला.',
    descriptionEn: 'Building permission approval in village boundaries.',
    fee: 100,
    deliveryDays: 15,
    requiredDocuments: ['मालमत्ता ७/१२ किंवा ८-अ', 'नकाशा', 'ना-हरकत अर्ज']
  }
];

// Development Projects
export const defaultProjects: Project[] = [
  {
    id: 'prj-1',
    titleMr: 'जल जीवन मिशन अंतर्गत घरोघरी नळ जोडणी व पाण्याची टाकी',
    titleEn: 'Jal Jeevan Mission Har Ghar Jal Pipeline',
    status: 'ONGOING',
    budget: '₹ ७५,००,०००',
    completionPercent: 70,
    scheme: 'जल जीवन मिशन'
  },
  {
    id: 'prj-2',
    titleMr: 'मुख्य रस्ता व बाजारपेठ सिमेंट काँक्रीट रस्ता (CC Road)',
    titleEn: 'Main Village Center Cement Concrete Road',
    status: 'COMPLETED',
    budget: '₹ ३५,००,०००',
    completionPercent: 100,
    scheme: 'जिल्हा नियोजन निधी'
  },
  {
    id: 'prj-3',
    titleMr: 'हायटेक डिजिटल ग्रामपंचायत कार्यालय व अभ्यासिका',
    titleEn: 'High-Tech Digital GP Office & Rural Library',
    status: 'COMPLETED',
    budget: '₹ २५,००,०००',
    completionPercent: 100,
    scheme: '१५ वा वित्त आयोग'
  },
  {
    id: 'prj-4',
    titleMr: 'घनकचरा व्यवस्थापन व सेंद्रिय खत प्रकल्प (Swachh Bharat)',
    titleEn: 'Solid Waste Management & Composting Project',
    status: 'ONGOING',
    budget: '₹ १५,००,०००',
    completionPercent: 55,
    scheme: 'स्वच्छ भारत मिशन ग्रामीण'
  }
];

// Water Schedules
export const defaultWaterSchedules: WaterSchedule[] = [
  {
    id: 'ws-1',
    gramPanchayatId: '172627',
    wardNumber: 'प्रभाग १',
    wardName: 'गावठाण, मुख्य बाजारपेठ व मारुती मंदिर परिसर',
    morningTime: 'सकाळी ०६:३० ते ०८:३०',
    eveningTime: 'सायंकाळी ०६:०० ते ०७:३०',
    status: 'ACTIVE',
    nextSupplyDate: 'दररोज नियमित',
    chlorinationDone: true,
    tankerAvailable: true,
    notes: 'जलशुद्धीकरण प्रकल्पातून क्लोरीनयुक्त शुद्ध पाणी पुरवले जाते.'
  },
  {
    id: 'ws-2',
    gramPanchayatId: '172627',
    wardNumber: 'प्रभाग २',
    wardName: 'जि. प. शाळा परिसर, नवीन वसाहत व आंबेडकर नगर',
    morningTime: 'सकाळी ०७:०० ते ०९:००',
    eveningTime: 'सायंकाळी ०६:३० ते ०८:००',
    status: 'ACTIVE',
    nextSupplyDate: 'दररोज नियमित',
    chlorinationDone: true,
    tankerAvailable: true,
    notes: 'दाब व्यवस्थित आहे.'
  },
  {
    id: 'ws-3',
    gramPanchayatId: '172627',
    wardNumber: 'प्रभाग ३',
    wardName: 'माळी गल्ली व हनुमान टेकडी परिसर',
    morningTime: 'सकाळी ०८:०० ते १०:००',
    eveningTime: 'सायंकाळी ०७:०० ते ०८:३०',
    status: 'ACTIVE',
    nextSupplyDate: 'दररोज नियमित',
    chlorinationDone: true,
    tankerAvailable: false
  },
  {
    id: 'ws-4',
    gramPanchayatId: '172627',
    wardNumber: 'प्रभाग ४',
    wardName: 'शेतशिवार व बाहेरील वस्ती',
    morningTime: 'सकाळी ०९:०० ते १०:३०',
    eveningTime: 'सायंकाळी ०७:३० ते ०९:००',
    status: 'ACTIVE',
    nextSupplyDate: 'दररोज नियमित',
    chlorinationDone: true,
    tankerAvailable: true
  }
];

// Default Notices
export const defaultNotices: Notice[] = [
  {
    id: 'not-1',
    gramPanchayatId: '172627',
    titleMr: '📢 महात्मा गांधी राष्ट्रीय ग्रामीण रोजगार (मनरेगा) अंतर्गत नवीन विहीर व गोठा अनुदान अर्ज',
    titleEn: '📢 MGNREGA Irrigation Well and Cattle Shed Subsidy Applications Open',
    contentMr: 'सर्व शेतकरी बांधवांना कळविण्यात येते की मनरेगा योजनेअंतर्गत सिंचन विहीर व शेळी-गाय गोठा अनुदानासाठी अर्ज स्वीकारणे सुरू झाले आहे. इच्छुकांनी ७/१२, ८-अ व बँक पासबुकसह कार्यालयात जमा करावे.',
    contentEn: 'Applications for MGNREGA farm wells and cattle shed subsidies are now open at the GP office. Eligible farmers please submit 7/12, 8-A and bank passbook.',
    category: 'GENERAL',
    publishedDate: '२०२६-०९-२८',
    isImportant: true
  },
  {
    id: 'not-2',
    gramPanchayatId: '172627',
    titleMr: '💧 गावातील जलकुंभांची स्वच्छता व निर्जंतुकीकरण मोहीम',
    titleEn: '💧 Periodic Water Reservoir Cleaning & Chlorination Drive',
    contentMr: 'गावातील सर्व मुख्य पाण्याच्या टाक्यांची स्वच्छता व टीसीएल पावडर टाकून निर्जंतुकीकरण करण्याचे काम ३ ऑक्टोबर रोजी होणार आहे. नागरिकांनी आदल्या दिवशी आवश्यक तेवढा पाणीसाठा करून ठेवावा.',
    contentEn: 'Cleaning and sanitization of water reservoirs is scheduled for Oct 3. Citizens are advised to store sufficient water in advance.',
    category: 'WATER',
    publishedDate: '२०२६-०९-३०',
    isImportant: false
  },
  {
    id: 'not-3',
    gramPanchayatId: '172627',
    titleMr: '🏛️ विशेष महिला ग्रामसभा व स्वयंसहाय्यता गट मार्गदर्शन मेळावा',
    titleEn: '🏛️ Special Women Gram Sabha & Self Help Group Guidance Camp',
    contentMr: 'उमेद महाराष्ट्र राज्य ग्रामीण जीवनोन्नती अभियानांतर्गत महिलांना लघुउद्योगांसाठी बिनव्याजी कर्ज व प्रशिक्षण देण्यासाठी विशेष सभा आयोजित केली आहे.',
    contentEn: 'Special Women Gram Sabha for self-help group entrepreneurship and interest-free loans is organized at GP auditorium.',
    category: 'GRAMSABHA',
    publishedDate: '२०२६-०९-२५',
    isImportant: true
  }
];

// Initial Complaints
export const initialComplaints: Complaint[] = [
  {
    id: 'CMP-2026-001',
    gramPanchayatId: '172627',
    citizenName: 'रमेश सखाराम पाटील',
    citizenPhone: '9876543210',
    wardNumber: 'प्रभाग १',
    category: 'पाणीपुरवठा (Water Supply)',
    title: 'मारुती मंदिर चौकातील मुख्य पाइपलाइन लीकेज दुरुस्त करणेबाबत',
    description: 'गेल्या ३ दिवसांपासून मारुती मंदिरासमोरील रस्त्यावर पाईप फुटल्याने पाणी वाया जात आहे व रस्त्यावर चिखल झाला आहे. कृपया तातडीने दुरुस्ती व्हावी.',
    status: 'IN_PROGRESS',
    priority: 'HIGH',
    submittedAt: '२०२६-०९-२९ १०:३०',
    assignedOfficer: 'श्री. गणेश जोशी (ग्रामसेवक)'
  },
  {
    id: 'CMP-2026-002',
    gramPanchayatId: '172627',
    citizenName: 'सुनीता प्रभाकर देशमुख',
    citizenPhone: '9123456789',
    wardNumber: 'प्रभाग ३',
    category: 'स्वच्छता व कचरा (Sanitation)',
    title: 'शाळेसमोरील कचराकुंडी नियमित उचलली जात नाही',
    description: 'जिल्हा परिषद शाळेसमोरील रस्त्यावर कचरा साठला असून दुर्गंधी सुटली आहे. लहान मुलांच्या आरोग्यासाठी त्वरित घंटागाडी पाठवावी.',
    status: 'RESOLVED',
    priority: 'NORMAL',
    submittedAt: '२०२६-०९-२७ ०९:१५',
    resolvedAt: '२०२६-०९-२८ १२:००',
    assignedOfficer: 'श्री. गणेश जोशी (ग्रामसेवक)',
    officerRemarks: 'आरोग्य सेवकांनी कचरा उचलून परिसर स्वच्छ केला आहे व औषध फवारणी केली आहे.'
  },
  {
    id: 'CMP-2026-003',
    gramPanchayatId: '172627',
    citizenName: 'विठ्ठल नारायण जाधव',
    citizenPhone: '9823112233',
    wardNumber: 'प्रभाग २',
    category: 'पथदिवे (Street Lights)',
    title: 'स्मशानभूमी रस्त्यावरील ३ पथदिवे गेल्या आठवड्यापासून बंद',
    description: 'रात्रीच्या वेळी अंधारामुळे नागरिकांना त्रास होतो. कृपया LED दिवे बदलण्यात यावेत.',
    status: 'SUBMITTED',
    priority: 'NORMAL',
    submittedAt: '२०२६-०९-३० ०८:००'
  }
];

export const defaultComplaints = initialComplaints;

export const getPanchayatProfileByGpId = (rawGpId: string): PanchayatProfile => {
  const gpId = normalizeLgdId(rawGpId) || '172627';
  const gp = lgdGetGramPanchayatById(gpId);
  const taluka = gp ? lgdGetTalukaById(gp.talukaId) : undefined;
  const dist = taluka ? lgdGetDistrictById(taluka.districtId) : undefined;

  const isPalaskhed = gpId === '172627' || gpId === 'gp_palaskhed_daulat';

  return {
    id: gpId,
    nameMr: gp?.nameMr || (isPalaskhed ? 'पळसखेड दौलत' : 'ग्रामपंचायत'),
    nameEn: gp?.nameEn || (isPalaskhed ? 'Palaskhed Daulat' : 'Grampanchayat'),
    districtMr: dist?.nameMr || 'बुलढाणा',
    districtEn: dist?.nameEn || 'Buldhana',
    talukaMr: taluka?.nameMr || 'चिखली',
    talukaEn: taluka?.nameEn || 'Chikhli',
    pincode: gp?.pincode || (isPalaskhed ? '443201' : '400001'),
    population: isPalaskhed ? 4850 : 3500,
    households: isPalaskhed ? 920 : 650,
    wardsCount: 6,
    sarpanchName: isPalaskhed ? 'सौ. सुजाता आनंदराव पाटील' : 'मा. सरपंच साहेब',
    sarpanchPhone: isPalaskhed ? '९८२२११४४५५' : '९८००००००००',
    gramsevakName: isPalaskhed ? 'श्री. गणेश बाळकृष्ण जोशी (VDO)' : 'मा. ग्रामविकास अधिकारी',
    gramsevakPhone: isPalaskhed ? '९४२३८८९९००' : '९४००००००००',
    officeAddress: `ग्रामपंचायत कार्यालय, ${gp?.nameMr || (isPalaskhed ? 'पळसखेड दौलत' : '')}, ता. ${taluka?.nameMr || (isPalaskhed ? 'चिखली' : '')}, जि. ${dist?.nameMr || (isPalaskhed ? 'बुलढाणा' : '')}`,
    officeHours: 'सकाळी १०:०० ते सायंकाळी ५:४५ (सोमवार ते शनिवार)',
    helplineNumber: isPalaskhed ? '०७२६४-२४२००१' : '०२२-२२०२४१३४'
  };
};

export const getWaterSchedulesByGpId = (rawGpId: string): WaterSchedule[] => {
  const gpId = normalizeLgdId(rawGpId);
  const filtered = defaultWaterSchedules.filter((ws) => ws.gramPanchayatId === gpId || ws.gramPanchayatId === rawGpId);
  if (filtered.length > 0) return filtered;

  return [
    {
      id: `ws-${gpId}-1`,
      gramPanchayatId: gpId,
      wardNumber: 'प्रभाग १',
      wardName: 'गावठाण व मुख्य बाजारपेठ',
      morningTime: 'सकाळी ०६:३० ते ०८:३०',
      eveningTime: 'सायंकाळी ०६:०० ते ०७:३०',
      status: 'ACTIVE',
      nextSupplyDate: 'दररोज नियमित',
      chlorinationDone: true,
      tankerAvailable: true,
      notes: 'नियमित शुद्ध पाणीपुरवठा सुरू आहे.'
    },
    {
      id: `ws-${gpId}-2`,
      gramPanchayatId: gpId,
      wardNumber: 'प्रभाग २',
      wardName: 'शाळा परिसर व नवीन वसाहत',
      morningTime: 'सकाळी ०७:०० ते ०९:००',
      eveningTime: 'सायंकाळी ०६:३० ते ०८:००',
      status: 'ACTIVE',
      nextSupplyDate: 'दररोज नियमित',
      chlorinationDone: true,
      tankerAvailable: true
    }
  ];
};

export const getNoticesByGpId = (rawGpId: string): Notice[] => {
  const gpId = normalizeLgdId(rawGpId);
  const filtered = defaultNotices.filter((n) => n.gramPanchayatId === gpId || n.gramPanchayatId === rawGpId);
  if (filtered.length > 0) return filtered;

  const gp = lgdGetGramPanchayatById(gpId);
  return [
    {
      id: `not-${gpId}-1`,
      gramPanchayatId: gpId,
      titleMr: `📢 ${gp?.nameMr || 'ग्रामपंचायत'} मासिक ग्रामसभा सूचना`,
      titleEn: `📢 Monthly Gram Sabha Notice - ${gp?.nameEn || 'Grampanchayat'}`,
      contentMr: 'सर्व ग्रामस्थांना सूचित करण्यात येते की मासिक ग्रामसभा ग्रामपंचायत कार्यालयात आयोजित केली आहे.',
      contentEn: 'All villagers are informed that the monthly Gram Sabha is scheduled at the GP office.',
      category: 'GRAMSABHA',
      publishedDate: '२०२६-०९-२९',
      isImportant: true
    }
  ];
};
