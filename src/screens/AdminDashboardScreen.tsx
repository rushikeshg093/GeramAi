import React, { useState } from 'react';
import {
  AppLanguage,
  AdminUser,
  PanchayatProfile,
  Complaint,
  WaterSchedule,
  TankerBooking,
  ServiceApplication,
  Notice
} from '../types';
import {
  Shield,
  FileText,
  AlertCircle,
  Droplets,
  Bell,
  PhoneCall,
  CheckCircle,
  Clock,
  Send,
  Building,
  Users,
  Search,
  Check,
  X,
  Volume2,
  Calendar,
  Sparkles,
  Radio
} from 'lucide-react';

interface AdminDashboardScreenProps {
  language: AppLanguage;
  adminUser: AdminUser;
  profile: PanchayatProfile;
  complaints: Complaint[];
  waterSchedules: WaterSchedule[];
  notices: Notice[];
  onUpdateComplaintStatus: (id: string, status: Complaint['status'], remarks: string) => void;
  onAddNotice: (notice: Omit<Notice, 'id'>) => void;
  onLogout: () => void;
}

export const AdminDashboardScreen: React.FC<AdminDashboardScreenProps> = ({
  language,
  adminUser,
  profile,
  complaints,
  waterSchedules,
  notices,
  onUpdateComplaintStatus,
  onAddNotice,
  onLogout
}) => {
  const [activeTab, setActiveTab] = useState<
    'overview' | 'complaints' | 'water' | 'notices' | 'ai_calls' | 'profile'
  >('overview');

  // Complaint remarks modal state
  const [selectedComplaint, setSelectedComplaint] = useState<Complaint | null>(null);
  const [newStatus, setNewStatus] = useState<Complaint['status']>('RESOLVED');
  const [remarksText, setRemarksText] = useState('');

  // Notice creation form state
  const [newNoticeTitle, setNewNoticeTitle] = useState('');
  const [newNoticeContent, setNewNoticeContent] = useState('');
  const [newNoticeCategory, setNewNoticeCategory] = useState<Notice['category']>('GRAMSABHA');
  const [newNoticeImportant, setNewNoticeImportant] = useState(false);
  const [noticeSuccessMsg, setNoticeSuccessMsg] = useState<string | null>(null);

  // AI Voice Call Simulation
  const [aiCampaignText, setAiCampaignText] = useState(
    'पळसखेड दौलत ग्रामस्थांना सूचित करण्यात येते की, येत्या सोमवारी सकाळी ११:०० वाजता महत्त्वाची ग्रामसभा आयोजित केली आहे.'
  );
  const [isCalling, setIsCalling] = useState(false);
  const [callProgressMsg, setCallProgressMsg] = useState<string | null>(null);

  const pendingComplaintsCount = complaints.filter(
    (c) => c.status === 'SUBMITTED' || c.status === 'IN_PROGRESS'
  ).length;

  const handleSaveComplaintStatus = () => {
    if (!selectedComplaint) return;
    onUpdateComplaintStatus(selectedComplaint.id, newStatus, remarksText);
    setSelectedComplaint(null);
    setRemarksText('');
  };

  const handlePublishNotice = (e: React.FormEvent) => {
    e.preventDefault();
    if (!newNoticeTitle.trim() || !newNoticeContent.trim()) return;

    onAddNotice({
      gramPanchayatId: adminUser.gramPanchayatId,
      titleMr: newNoticeTitle,
      titleEn: newNoticeTitle,
      contentMr: newNoticeContent,
      contentEn: newNoticeContent,
      category: newNoticeCategory,
      publishedDate: new Date().toISOString().split('T')[0],
      isImportant: newNoticeImportant
    });

    setNewNoticeTitle('');
    setNewNoticeContent('');
    setNoticeSuccessMsg(
      language === 'mr' ? 'नोटीस यशस्वीरीत्या प्रकाशित झाली!' : 'Notice published successfully!'
    );
    setTimeout(() => setNoticeSuccessMsg(null), 3000);
  };

  const handleStartAiBroadcast = () => {
    setIsCalling(true);
    setCallProgressMsg(
      language === 'mr'
        ? 'पळसखेड दौलत मधील ९२० नोंदणीकृत नागरिकांना स्वयंचलित AI कॉल डायल केले जात आहेत...'
        : 'Broadcasting automated AI voice call to 920 registered village households...'
    );

    setTimeout(() => {
      setIsCalling(false);
      setCallProgressMsg(
        language === 'mr'
          ? '✓ व्हॉइस कॉल मोहीम पूर्ण! ८८२ नागरिकांनी कॉल स्वीकारला.'
          : '✓ AI Voice broadcast complete! 882 citizens answered.'
      );
      setTimeout(() => setCallProgressMsg(null), 5000);
    }, 3000);
  };

  return (
    <div className="min-h-screen bg-stone-50 pb-12">
      {/* Officer Header Banner */}
      <div className="bg-gradient-to-r from-orange-700 via-amber-700 to-orange-800 text-white py-6 px-4">
        <div className="max-w-7xl mx-auto flex flex-col md:flex-row md:items-center justify-between gap-4">
          <div className="flex items-center gap-4">
            <div className="w-16 h-16 rounded-2xl bg-white/10 backdrop-blur border border-white/20 flex items-center justify-center text-3xl shrink-0">
              🏛️
            </div>
            <div>
              <div className="flex items-center gap-2">
                <span className="px-2.5 py-0.5 rounded-full bg-emerald-500/20 border border-emerald-400 text-emerald-200 text-xs font-bold uppercase tracking-wider">
                  {language === 'mr' ? 'सक्रिय अधिकारी' : 'Active Officer'}
                </span>
                <span className="text-white/60 text-xs font-mono">ID: {adminUser.adminId}</span>
              </div>
              <h1 className="text-xl sm:text-2xl font-black text-white mt-1">
                {adminUser.name}
              </h1>
              <p className="text-xs text-orange-200 font-medium">
                {adminUser.designation} • {profile.nameMr}
              </p>
              <div className="text-xs text-white/80 mt-1 flex items-center gap-2">
                <span>📍 ता. {profile.talukaMr}, जि. {profile.districtMr} (पिनकोड: {profile.pincode})</span>
              </div>
            </div>
          </div>

          <div className="flex items-center gap-2 self-start md:self-center">
            <button
              onClick={() => setActiveTab('ai_calls')}
              className="px-3.5 py-2 rounded-xl bg-white/15 hover:bg-white/25 border border-white/20 text-white text-xs font-bold flex items-center gap-1.5 transition"
            >
              <Radio className="w-4 h-4 text-emerald-300 animate-pulse" />
              <span>{language === 'mr' ? 'AI व्हॉइस ब्रॉडकास्ट' : 'AI Voice Calls'}</span>
            </button>
            <button
              onClick={onLogout}
              className="px-3.5 py-2 rounded-xl bg-white text-orange-800 hover:bg-orange-50 text-xs font-bold shadow-sm transition"
            >
              {language === 'mr' ? 'लॉग आउट' : 'Logout'}
            </button>
          </div>
        </div>
      </div>

      {/* Navigation Tabs */}
      <div className="bg-white border-b border-stone-200 sticky top-[73px] z-30">
        <div className="max-w-7xl mx-auto px-4 flex items-center gap-2 overflow-x-auto py-2.5">
          <button
            onClick={() => setActiveTab('overview')}
            className={`px-4 py-2 rounded-xl text-xs font-bold whitespace-nowrap transition flex items-center gap-1.5 ${
              activeTab === 'overview'
                ? 'bg-orange-600 text-white shadow-xs'
                : 'text-stone-600 hover:bg-stone-100'
            }`}
          >
            <span>{language === 'mr' ? 'डॅशबोर्ड सारांश' : 'Dashboard Overview'}</span>
          </button>

          <button
            onClick={() => setActiveTab('complaints')}
            className={`px-4 py-2 rounded-xl text-xs font-bold whitespace-nowrap transition flex items-center gap-1.5 ${
              activeTab === 'complaints'
                ? 'bg-orange-600 text-white shadow-xs'
                : 'text-stone-600 hover:bg-stone-100'
            }`}
          >
            <AlertCircle className="w-3.5 h-3.5" />
            <span>{language === 'mr' ? 'तक्रार निवारण' : 'Grievance Redressal'}</span>
            {pendingComplaintsCount > 0 && (
              <span className="ml-1 px-1.5 py-0.2 rounded-full bg-amber-200 text-amber-900 text-[10px] font-black">
                {pendingComplaintsCount}
              </span>
            )}
          </button>

          <button
            onClick={() => setActiveTab('water')}
            className={`px-4 py-2 rounded-xl text-xs font-bold whitespace-nowrap transition flex items-center gap-1.5 ${
              activeTab === 'water'
                ? 'bg-orange-600 text-white shadow-xs'
                : 'text-stone-600 hover:bg-stone-100'
            }`}
          >
            <Droplets className="w-3.5 h-3.5" />
            <span>{language === 'mr' ? 'पाणीपुरवठा व्यवस्थापन' : 'Water Management'}</span>
          </button>

          <button
            onClick={() => setActiveTab('notices')}
            className={`px-4 py-2 rounded-xl text-xs font-bold whitespace-nowrap transition flex items-center gap-1.5 ${
              activeTab === 'notices'
                ? 'bg-orange-600 text-white shadow-xs'
                : 'text-stone-600 hover:bg-stone-100'
            }`}
          >
            <Bell className="w-3.5 h-3.5" />
            <span>{language === 'mr' ? 'नोटीस बोर्ड प्रसिद्ध करा' : 'Notices'}</span>
          </button>

          <button
            onClick={() => setActiveTab('ai_calls')}
            className={`px-4 py-2 rounded-xl text-xs font-bold whitespace-nowrap transition flex items-center gap-1.5 ${
              activeTab === 'ai_calls'
                ? 'bg-orange-600 text-white shadow-xs'
                : 'text-stone-600 hover:bg-stone-100'
            }`}
          >
            <PhoneCall className="w-3.5 h-3.5" />
            <span>{language === 'mr' ? '🤖 AI व्हॉइस कॉल सिस्टीम' : '🤖 AI Call Broadcast'}</span>
          </button>

          <button
            onClick={() => setActiveTab('profile')}
            className={`px-4 py-2 rounded-xl text-xs font-bold whitespace-nowrap transition flex items-center gap-1.5 ${
              activeTab === 'profile'
                ? 'bg-orange-600 text-white shadow-xs'
                : 'text-stone-600 hover:bg-stone-100'
            }`}
          >
            <Building className="w-3.5 h-3.5" />
            <span>{language === 'mr' ? 'ग्रामपंचायत माहिती' : 'Village Info'}</span>
          </button>
        </div>
      </div>

      {/* Main Content Area */}
      <div className="max-w-7xl mx-auto px-4 py-6">
        {/* ================= TAB 1: OVERVIEW ================= */}
        {activeTab === 'overview' && (
          <div className="space-y-6">
            {/* Stat Cards */}
            <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
              <div className="bg-white p-5 rounded-2xl border border-stone-200 shadow-xs">
                <div className="flex items-center justify-between text-xs text-stone-500 font-semibold mb-2">
                  <span>{language === 'mr' ? 'एकूण लोकसंख्या' : 'Population'}</span>
                  <Users className="w-4 h-4 text-orange-500" />
                </div>
                <div className="text-2xl font-black text-stone-900">{profile.population.toLocaleString('en-IN')}</div>
                <div className="text-xs text-stone-500 mt-1">{profile.households} {language === 'mr' ? 'कुटुंबे' : 'Households'}</div>
              </div>

              <div className="bg-white p-5 rounded-2xl border border-stone-200 shadow-xs">
                <div className="flex items-center justify-between text-xs text-stone-500 font-semibold mb-2">
                  <span>{language === 'mr' ? 'प्रलंबित तक्रारी' : 'Pending Complaints'}</span>
                  <AlertCircle className="w-4 h-4 text-amber-500" />
                </div>
                <div className="text-2xl font-black text-amber-600">{pendingComplaintsCount}</div>
                <div className="text-xs text-stone-500 mt-1">{complaints.length} {language === 'mr' ? 'एकूण प्राप्त' : 'Total Received'}</div>
              </div>

              <div className="bg-white p-5 rounded-2xl border border-stone-200 shadow-xs">
                <div className="flex items-center justify-between text-xs text-stone-500 font-semibold mb-2">
                  <span>{language === 'mr' ? 'प्रभाग पाणीपुरवठा' : 'Water Supply'}</span>
                  <Droplets className="w-4 h-4 text-sky-500" />
                </div>
                <div className="text-2xl font-black text-sky-600">{waterSchedules.length} {language === 'mr' ? 'प्रभाग' : 'Wards'}</div>
                <div className="text-xs text-emerald-600 mt-1">✓ {language === 'mr' ? 'शुद्धीकरण नियमित' : 'Chlorination Active'}</div>
              </div>

              <div className="bg-white p-5 rounded-2xl border border-stone-200 shadow-xs">
                <div className="flex items-center justify-between text-xs text-stone-500 font-semibold mb-2">
                  <span>{language === 'mr' ? 'सक्रिय सूचना' : 'Active Notices'}</span>
                  <Bell className="w-4 h-4 text-purple-500" />
                </div>
                <div className="text-2xl font-black text-purple-600">{notices.length}</div>
                <div className="text-xs text-stone-500 mt-1">{language === 'mr' ? 'ग्रामसभा व पाणी वेळापत्रक' : 'Gramsabha & Alerts'}</div>
              </div>
            </div>

            {/* Quick Grievance Action Section */}
            <div className="bg-white rounded-2xl border border-stone-200 p-5 shadow-xs">
              <div className="flex items-center justify-between mb-4">
                <h2 className="text-base font-bold text-stone-900 flex items-center gap-2">
                  <AlertCircle className="w-5 h-5 text-orange-600" />
                  <span>
                    {language === 'mr' ? 'नागरिकांच्या ताज्या तक्रारी' : 'Recent Citizen Complaints'}
                  </span>
                </h2>
                <button
                  onClick={() => setActiveTab('complaints')}
                  className="text-xs font-bold text-orange-600 hover:text-orange-700"
                >
                  {language === 'mr' ? 'सर्व पहा →' : 'View All →'}
                </button>
              </div>

              <div className="space-y-3">
                {complaints.slice(0, 3).map((comp) => (
                  <div
                    key={comp.id}
                    className="p-4 rounded-xl border border-stone-200 bg-stone-50 flex flex-col md:flex-row md:items-center justify-between gap-3"
                  >
                    <div>
                      <div className="flex items-center gap-2 mb-1">
                        <span className="text-xs font-mono font-bold text-stone-500">{comp.id}</span>
                        <span className="px-2 py-0.5 rounded text-[10px] font-bold bg-stone-200 text-stone-700">
                          {comp.wardNumber}
                        </span>
                        <span
                          className={`px-2 py-0.5 rounded text-[10px] font-bold ${
                            comp.status === 'RESOLVED'
                              ? 'bg-emerald-100 text-emerald-800'
                              : comp.status === 'IN_PROGRESS'
                              ? 'bg-amber-100 text-amber-800'
                              : 'bg-red-100 text-red-800'
                          }`}
                        >
                          {comp.status === 'RESOLVED'
                            ? 'निवारण पूर्ण (Resolved)'
                            : comp.status === 'IN_PROGRESS'
                            ? 'कार्यवाही चालू (In Progress)'
                            : 'नवीन तक्रार (Submitted)'}
                        </span>
                      </div>
                      <div className="text-sm font-bold text-stone-900">{comp.title}</div>
                      <p className="text-xs text-stone-600 line-clamp-1 mt-0.5">{comp.description}</p>
                      <div className="text-[11px] text-stone-400 mt-1">
                        {comp.citizenName} • {comp.citizenPhone} • {comp.submittedAt}
                      </div>
                    </div>

                    <button
                      onClick={() => {
                        setSelectedComplaint(comp);
                        setNewStatus(comp.status);
                        setRemarksText(comp.officerRemarks || '');
                      }}
                      className="px-3 py-1.5 rounded-lg bg-orange-600 hover:bg-orange-700 text-white text-xs font-bold shrink-0 transition"
                    >
                      {language === 'mr' ? 'शेरा व स्थिती बदला' : 'Update Status'}
                    </button>
                  </div>
                ))}
              </div>
            </div>
          </div>
        )}

        {/* ================= TAB 2: COMPLAINTS ================= */}
        {activeTab === 'complaints' && (
          <div className="bg-white rounded-2xl border border-stone-200 p-6 shadow-xs">
            <div className="flex items-center justify-between mb-6">
              <div>
                <h2 className="text-lg font-bold text-stone-900">
                  {language === 'mr' ? 'तक्रार निवारण व्यवस्थापन कक्ष' : 'Grievance Redressal Portal'}
                </h2>
                <p className="text-xs text-stone-500">
                  {language === 'mr'
                    ? 'पळसखेड दौलत मधील नागरिकांच्या प्राप्त तक्रारींवर अधिकृत कारवाई करा व शेरा द्या'
                    : 'Process and resolve citizen grievances for Palaskhed Daulat with official remarks'}
                </p>
              </div>
            </div>

            <div className="space-y-4">
              {complaints.map((comp) => (
                <div
                  key={comp.id}
                  className="p-4 rounded-xl border border-stone-200 bg-stone-50 flex flex-col md:flex-row md:items-start justify-between gap-4"
                >
                  <div className="space-y-1.5 flex-1">
                    <div className="flex flex-wrap items-center gap-2">
                      <span className="font-mono text-xs font-bold text-stone-600">{comp.id}</span>
                      <span className="px-2 py-0.5 rounded text-[10px] font-bold bg-orange-100 text-orange-800">
                        {comp.category}
                      </span>
                      <span className="px-2 py-0.5 rounded text-[10px] font-bold bg-stone-200 text-stone-700">
                        {comp.wardNumber}
                      </span>
                      <span
                        className={`px-2 py-0.5 rounded text-[10px] font-bold ${
                          comp.status === 'RESOLVED'
                            ? 'bg-emerald-100 text-emerald-800'
                            : comp.status === 'IN_PROGRESS'
                            ? 'bg-amber-100 text-amber-800'
                            : 'bg-red-100 text-red-800'
                        }`}
                      >
                        {comp.status}
                      </span>
                    </div>

                    <div className="text-base font-bold text-stone-900">{comp.title}</div>
                    <p className="text-xs text-stone-700 leading-relaxed">{comp.description}</p>

                    <div className="text-xs text-stone-500">
                      नागरिक: <span className="font-semibold text-stone-800">{comp.citizenName}</span> ({comp.citizenPhone}) • दिनांक: {comp.submittedAt}
                    </div>

                    {comp.officerRemarks && (
                      <div className="mt-2 p-2.5 rounded-lg bg-orange-50 border border-orange-200 text-xs">
                        <span className="font-bold text-orange-950">
                          {language === 'mr' ? 'अधिकृत अधिकारी शेरा: ' : 'Officer Remarks: '}
                        </span>
                        <span className="text-orange-900">{comp.officerRemarks}</span>
                        {comp.assignedOfficer && (
                          <div className="text-[10px] text-orange-700 mt-0.5">— {comp.assignedOfficer}</div>
                        )}
                      </div>
                    )}
                  </div>

                  <button
                    onClick={() => {
                      setSelectedComplaint(comp);
                      setNewStatus(comp.status);
                      setRemarksText(comp.officerRemarks || '');
                    }}
                    className="px-4 py-2 rounded-xl bg-orange-600 hover:bg-orange-700 text-white text-xs font-bold shrink-0 transition"
                  >
                    {language === 'mr' ? 'शेरा व स्थिती बदला' : 'Update Status'}
                  </button>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* ================= TAB 3: WATER ================= */}
        {activeTab === 'water' && (
          <div className="bg-white rounded-2xl border border-stone-200 p-6 shadow-xs">
            <h2 className="text-lg font-bold text-stone-900 mb-1">
              {language === 'mr' ? 'पाणीपुरवठा व वेळापत्रक व्यवस्थापन' : 'Water Supply Schedules'}
            </h2>
            <p className="text-xs text-stone-500 mb-6">
              {language === 'mr'
                ? 'पळसखेड दौलत प्रभागनिहाय पाणीपुरवठा स्थिती व दुरुस्ती सूचना'
                : 'Ward-wise water timings, chlorination and tanker availability'}
            </p>

            <div className="grid md:grid-cols-2 gap-4">
              {waterSchedules.map((ws) => (
                <div key={ws.id} className="p-4 rounded-xl border border-stone-200 bg-stone-50 space-y-2">
                  <div className="flex items-center justify-between">
                    <span className="text-sm font-bold text-stone-900">{ws.wardNumber} - {ws.wardName}</span>
                    <span
                      className={`px-2 py-0.5 rounded text-[10px] font-bold ${
                        ws.status === 'ACTIVE'
                          ? 'bg-emerald-100 text-emerald-800'
                          : ws.status === 'DELAYED'
                          ? 'bg-amber-100 text-amber-800'
                          : 'bg-red-100 text-red-800'
                      }`}
                    >
                      {ws.status}
                    </span>
                  </div>

                  <div className="text-xs text-stone-600 space-y-1">
                    <div>🌅 {language === 'mr' ? 'सकाळी' : 'Morning'}: <span className="font-semibold text-stone-800">{ws.morningTime}</span></div>
                    <div>🌆 {language === 'mr' ? 'सायंकाळी' : 'Evening'}: <span className="font-semibold text-stone-800">{ws.eveningTime}</span></div>
                    <div>💧 {language === 'mr' ? 'जलशुद्धीकरण (Chlorination)' : 'Chlorination'}: {ws.chlorinationDone ? '✓ पूर्ण' : 'प्रलंबित'}</div>
                  </div>

                  {ws.notes && (
                    <div className="p-2 rounded bg-amber-50 border border-amber-200 text-xs text-amber-900">
                      ℹ️ {ws.notes}
                    </div>
                  )}
                </div>
              ))}
            </div>
          </div>
        )}

        {/* ================= TAB 4: NOTICES ================= */}
        {activeTab === 'notices' && (
          <div className="space-y-6">
            {/* Publish Form */}
            <div className="bg-white rounded-2xl border border-stone-200 p-6 shadow-xs">
              <h2 className="text-lg font-bold text-stone-900 mb-1">
                {language === 'mr' ? 'नवीन नोटीस प्रकाशित करा' : 'Publish New Notice'}
              </h2>
              <p className="text-xs text-stone-500 mb-4">
                {language === 'mr'
                  ? 'पळसखेड दौलत ग्रामस्थांसाठी डिजिटल नोटीस बोर्डवर सूचना प्रसिद्ध करा'
                  : 'Broadcast notice to village notice board'}
              </p>

              {noticeSuccessMsg && (
                <div className="mb-4 p-3 rounded-xl bg-emerald-50 border border-emerald-200 text-xs text-emerald-700 font-bold">
                  ✓ {noticeSuccessMsg}
                </div>
              )}

              <form onSubmit={handlePublishNotice} className="space-y-4">
                <div>
                  <label className="block text-xs font-bold text-stone-700 mb-1">
                    {language === 'mr' ? 'नोटीस शीर्षक *' : 'Notice Title *'}
                  </label>
                  <input
                    type="text"
                    value={newNoticeTitle}
                    onChange={(e) => setNewNoticeTitle(e.target.value)}
                    placeholder="उदा. विशेष ग्रामसभा आयोजन किंवा पाणीपुरवठा सूचना"
                    className="w-full px-3.5 py-2.5 text-sm rounded-xl border border-stone-300 focus:outline-none focus:border-orange-500"
                    required
                  />
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-xs font-bold text-stone-700 mb-1">
                      {language === 'mr' ? 'वर्गवारी (Category)' : 'Category'}
                    </label>
                    <select
                      value={newNoticeCategory}
                      onChange={(e) => setNewNoticeCategory(e.target.value as Notice['category'])}
                      className="w-full px-3.5 py-2.5 text-sm rounded-xl border border-stone-300 bg-white"
                    >
                      <option value="GRAMSABHA">{language === 'mr' ? 'ग्रामसभा' : 'Gram Sabha'}</option>
                      <option value="WATER">{language === 'mr' ? 'पाणीपुरवठा' : 'Water Supply'}</option>
                      <option value="TAX">{language === 'mr' ? 'कर आकारणी व सवलत' : 'Tax & Schemes'}</option>
                      <option value="HEALTH">{language === 'mr' ? 'आरोग्य व स्वच्छता' : 'Health'}</option>
                      <option value="GENERAL">{language === 'mr' ? 'सामान्य सूचना' : 'General'}</option>
                    </select>
                  </div>

                  <div className="flex items-center gap-2 pt-6">
                    <input
                      type="checkbox"
                      id="important-check"
                      checked={newNoticeImportant}
                      onChange={(e) => setNewNoticeImportant(e.target.checked)}
                      className="w-4 h-4 rounded text-orange-600 focus:ring-orange-500"
                    />
                    <label htmlFor="important-check" className="text-xs font-bold text-stone-700">
                      {language === 'mr' ? 'तातडीची / महत्त्वाची सूचना' : 'Mark as Urgent'}
                    </label>
                  </div>
                </div>

                <div>
                  <label className="block text-xs font-bold text-stone-700 mb-1">
                    {language === 'mr' ? 'सूचना मजकूर *' : 'Notice Content *'}
                  </label>
                  <textarea
                    rows={3}
                    value={newNoticeContent}
                    onChange={(e) => setNewNoticeContent(e.target.value)}
                    placeholder="सर्व ग्रामस्थांना सूचित करण्यात येते की..."
                    className="w-full px-3.5 py-2.5 text-sm rounded-xl border border-stone-300 focus:outline-none focus:border-orange-500"
                    required
                  />
                </div>

                <button
                  type="submit"
                  className="px-5 py-2.5 rounded-xl bg-orange-600 hover:bg-orange-700 text-white font-bold text-xs shadow-xs transition flex items-center gap-2"
                >
                  <Send className="w-3.5 h-3.5" />
                  <span>{language === 'mr' ? 'नोटीस प्रसिद्ध करा' : 'Publish Notice'}</span>
                </button>
              </form>
            </div>

            {/* List */}
            <div className="space-y-3">
              {notices.map((n) => (
                <div key={n.id} className="bg-white p-4 rounded-xl border border-stone-200">
                  <div className="flex items-center justify-between mb-1">
                    <span className="text-xs font-bold text-orange-600 uppercase">{n.category}</span>
                    <span className="text-xs text-stone-400">{n.publishedDate}</span>
                  </div>
                  <h3 className="text-sm font-bold text-stone-900">{n.titleMr}</h3>
                  <p className="text-xs text-stone-600 mt-1 leading-relaxed">{n.contentMr}</p>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* ================= TAB 5: AI CALLS ================= */}
        {activeTab === 'ai_calls' && (
          <div className="bg-white rounded-2xl border border-stone-200 p-6 shadow-xs max-w-3xl">
            <div className="flex items-center gap-3 mb-4">
              <div className="w-12 h-12 rounded-xl bg-orange-100 text-orange-600 flex items-center justify-center">
                <Radio className="w-6 h-6 animate-pulse" />
              </div>
              <div>
                <h2 className="text-lg font-bold text-stone-900">
                  {language === 'mr' ? '🤖 AI स्वयंचलित व्हॉइस कॉल ब्रॉडकास्ट' : '🤖 AI Automated Citizen Voice Broadcast'}
                </h2>
                <p className="text-xs text-stone-500">
                  {language === 'mr'
                    ? 'पळसखेड दौलत मधील सर्व नागरिकांना एकाच वेळी मराठीत AI फोन कॉल करा'
                    : 'Broadcast automated outbound calls in Marathi to registered village citizens'}
                </p>
              </div>
            </div>

            <div className="space-y-4">
              <div>
                <label className="block text-xs font-bold text-stone-700 mb-1">
                  {language === 'mr' ? 'AI व्हॉइस कॉल संदेश मजकूर *' : 'Voice Broadcast Message *'}
                </label>
                <textarea
                  rows={4}
                  value={aiCampaignText}
                  onChange={(e) => setAiCampaignText(e.target.value)}
                  className="w-full px-3.5 py-2.5 text-sm rounded-xl border border-stone-300 focus:outline-none focus:border-orange-500 font-medium"
                />
              </div>

              <div className="flex flex-wrap gap-2 text-xs">
                <button
                  type="button"
                  onClick={() =>
                    setAiCampaignText(
                      'नमस्कार, पळसखेड दौलत ग्रामपंचायतीकडून सूचना. येत्या सोमवारी सकाळी ११ वाजता महत्त्वाची ग्रामसभा आहे. सर्वांनी उपस्थित राहावे.'
                    )
                  }
                  className="px-2.5 py-1 rounded bg-stone-100 hover:bg-stone-200 text-stone-700 font-medium"
                >
                  📢 ग्रामसभा संदेश
                </button>
                <button
                  type="button"
                  onClick={() =>
                    setAiCampaignText(
                      'नमस्कार, जलवाहिनी दुरुस्तीमुळे प्रभाग २ मधील पाणीपुरवठा सायंकाळी ६ वाजता होईल. कृपया नोंद घ्यावी.'
                    )
                  }
                  className="px-2.5 py-1 rounded bg-stone-100 hover:bg-stone-200 text-stone-700 font-medium"
                >
                  💧 पाणीपुरवठा सूचना
                </button>
                <button
                  type="button"
                  onClick={() =>
                    setAiCampaignText(
                      'नमस्कार, ग्रामपंचायत कर भरणाऱ्या नागरिकांना १५ ऑक्टोबरपर्यंत ५ टक्के विशेष सूट आहे. त्वरित लाभ घ्यावा.'
                    )
                  }
                  className="px-2.5 py-1 rounded bg-stone-100 hover:bg-stone-200 text-stone-700 font-medium"
                >
                  📋 कर सवलत योजना
                </button>
              </div>

              {callProgressMsg && (
                <div className="p-3.5 rounded-xl bg-orange-50 border border-orange-200 text-xs font-semibold text-orange-900 flex items-center gap-2">
                  <Volume2 className="w-4 h-4 text-orange-600 animate-bounce" />
                  <span>{callProgressMsg}</span>
                </div>
              )}

              <button
                type="button"
                onClick={handleStartAiBroadcast}
                disabled={isCalling}
                className="w-full py-3 rounded-xl bg-orange-600 hover:bg-orange-700 disabled:opacity-50 text-white font-bold text-sm shadow-sm transition flex items-center justify-center gap-2"
              >
                <PhoneCall className="w-4 h-4" />
                <span>
                  {isCalling
                    ? language === 'mr'
                      ? 'कॉलिंग चालू आहे...'
                      : 'Calling in progress...'
                    : language === 'mr'
                    ? '📞 सर्व नागरिकांना AI कॉल पाठवा (९२० कुटुंबे)'
                    : '📞 Launch AI Voice Call to 920 Households'}
                </span>
              </button>
            </div>
          </div>
        )}

        {/* ================= TAB 6: PROFILE ================= */}
        {activeTab === 'profile' && (
          <div className="bg-white rounded-2xl border border-stone-200 p-6 shadow-xs max-w-3xl space-y-4">
            <h2 className="text-lg font-bold text-stone-900">
              {language === 'mr' ? 'ग्रामपंचायत कार्यालय तपशील' : 'Grampanchayat Official Profile'}
            </h2>

            <div className="grid sm:grid-cols-2 gap-4 text-xs">
              <div className="p-3 rounded-xl bg-stone-50 border border-stone-200">
                <span className="text-stone-400 block mb-0.5">ग्रामपंचायत नाव</span>
                <span className="font-bold text-stone-900 text-sm">{profile.nameMr}</span>
              </div>
              <div className="p-3 rounded-xl bg-stone-50 border border-stone-200">
                <span className="text-stone-400 block mb-0.5">जिल्हा व तालुका</span>
                <span className="font-bold text-stone-900 text-sm">जि. {profile.districtMr}, ता. {profile.talukaMr}</span>
              </div>
              <div className="p-3 rounded-xl bg-stone-50 border border-stone-200">
                <span className="text-stone-400 block mb-0.5">सरपंच नाव व फोन</span>
                <span className="font-bold text-stone-900 text-sm">{profile.sarpanchName} ({profile.sarpanchPhone})</span>
              </div>
              <div className="p-3 rounded-xl bg-stone-50 border border-stone-200">
                <span className="text-stone-400 block mb-0.5">ग्रामसेवक नाव व फोन</span>
                <span className="font-bold text-stone-900 text-sm">{profile.gramsevakName} ({profile.gramsevakPhone})</span>
              </div>
              <div className="sm:col-span-2 p-3 rounded-xl bg-stone-50 border border-stone-200">
                <span className="text-stone-400 block mb-0.5">कार्यालय पत्ता व वेळ</span>
                <span className="font-bold text-stone-900">{profile.officeAddress}</span>
                <span className="text-stone-500 block mt-1">{profile.officeHours} • हेल्पलाईन: {profile.helplineNumber}</span>
              </div>
            </div>
          </div>
        )}
      </div>

      {/* Complaint Update Modal */}
      {selectedComplaint && (
        <div className="fixed inset-0 z-50 bg-black/40 backdrop-blur-xs flex items-center justify-center p-4">
          <div className="bg-white rounded-2xl max-w-md w-full p-6 shadow-xl border border-stone-200">
            <h3 className="text-lg font-bold text-stone-900 mb-1">
              {language === 'mr' ? 'तक्रार निवारण व शेरा नोंदवा' : 'Update Grievance'}
            </h3>
            <p className="text-xs text-stone-500 mb-4">
              {selectedComplaint.id} • {selectedComplaint.title}
            </p>

            <div className="space-y-4">
              <div>
                <label className="block text-xs font-bold text-stone-700 mb-1">
                  {language === 'mr' ? 'तक्रार स्थिती (Status) *' : 'Status *'}
                </label>
                <select
                  value={newStatus}
                  onChange={(e) => setNewStatus(e.target.value as Complaint['status'])}
                  className="w-full px-3 py-2 text-sm rounded-xl border border-stone-300 bg-white"
                >
                  <option value="SUBMITTED">नोंदणी झाली (Submitted)</option>
                  <option value="IN_PROGRESS">कार्यवाही चालू (In Progress)</option>
                  <option value="RESOLVED">निवारण पूर्ण (Resolved)</option>
                  <option value="REJECTED">नामंजूर / चुकीची तक्रार (Rejected)</option>
                </select>
              </div>

              <div>
                <label className="block text-xs font-bold text-stone-700 mb-1">
                  {language === 'mr' ? 'अधिकाऱ्याचा अधिकृत शेरा (Remarks) *' : 'Officer Remarks *'}
                </label>
                <textarea
                  rows={3}
                  value={remarksText}
                  onChange={(e) => setRemarksText(e.target.value)}
                  placeholder="उदा. कर्मचाऱ्याला पाठवून दुरुस्ती करण्यात आली आहे..."
                  className="w-full px-3 py-2 text-sm rounded-xl border border-stone-300 focus:outline-none focus:border-orange-500"
                />
              </div>

              <div className="flex items-center justify-end gap-2 pt-2">
                <button
                  type="button"
                  onClick={() => setSelectedComplaint(null)}
                  className="px-4 py-2 rounded-xl text-xs font-semibold text-stone-600 hover:bg-stone-100"
                >
                  {language === 'mr' ? 'रद्द करा' : 'Cancel'}
                </button>
                <button
                  type="button"
                  onClick={handleSaveComplaintStatus}
                  className="px-4 py-2 rounded-xl bg-orange-600 hover:bg-orange-700 text-white text-xs font-bold shadow-xs transition"
                >
                  {language === 'mr' ? 'शेरा जतन करा' : 'Save Remarks'}
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
