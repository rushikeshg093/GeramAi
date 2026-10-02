import React, { useState } from 'react';
import {
  AppLanguage,
  ScreenDestination,
  AdminUser,
  CitizenUser,
  Complaint,
  WaterSchedule,
  Notice,
  PanchayatProfile,
  PreapprovedOfficer
} from './types';
import {
  defaultComplaints,
  defaultWaterSchedules,
  defaultNotices,
  preapprovedOfficers,
  getPanchayatProfileByGpId,
  getWaterSchedulesByGpId,
  getNoticesByGpId,
  getGramPanchayatById,
  getTalukaById,
  getDistrictById
} from './data/maharashtraData';
import { Header } from './components/Header';
import { GpChangeModal } from './components/GpChangeModal';
import { RoleSelectionScreen } from './screens/RoleSelectionScreen';
import { AdminLoginScreen } from './screens/AdminLoginScreen';
import { AdminActivationScreen } from './screens/AdminActivationScreen';
import { AdminDashboardScreen } from './screens/AdminDashboardScreen';
import { CitizenAuthScreen } from './screens/CitizenAuthScreen';
import { CitizenHomeScreen } from './screens/CitizenHomeScreen';
import { CitizenComplaintsScreen } from './screens/CitizenComplaintsScreen';
import { CitizenWaterScreen } from './screens/CitizenWaterScreen';
import { CitizenServicesScreen } from './screens/CitizenServicesScreen';
import { CitizenNoticesScreen } from './screens/CitizenNoticesScreen';
import { CitizenInfoScreen } from './screens/CitizenInfoScreen';
import { AiAssistantScreen } from './screens/AiAssistantScreen';

export const App: React.FC = () => {
  const [language, setLanguage] = useState<AppLanguage>('mr');
  const [currentScreen, setCurrentScreen] = useState<ScreenDestination>('ROLE_SELECTION');
  const [adminUser, setAdminUser] = useState<AdminUser | null>(null);
  const [citizenUser, setCitizenUser] = useState<CitizenUser | null>(null);

  // Active Gram Panchayat context (Maharashtra -> District -> Taluka -> GP)
  const [activeGpId, setActiveGpId] = useState<string>('172627');
  const [isGpChangeModalOpen, setIsGpChangeModalOpen] = useState(false);

  // Persistent storage arrays
  const [complaints, setComplaints] = useState<Complaint[]>(defaultComplaints);
  const [waterSchedules] = useState<WaterSchedule[]>(defaultWaterSchedules);
  const [notices, setNotices] = useState<Notice[]>(defaultNotices);

  const toggleLanguage = () => {
    setLanguage((prev) => (prev === 'mr' ? 'en' : 'mr'));
  };

  const handleLogout = () => {
    setAdminUser(null);
    setCitizenUser(null);
    setCurrentScreen('ROLE_SELECTION');
  };

  const currentProfile: PanchayatProfile = getPanchayatProfileByGpId(
    adminUser ? adminUser.gramPanchayatId : activeGpId
  );

  // Filtered data isolated by Gram Panchayat
  const activeGpComplaints = complaints.filter(
    (c) => c.gramPanchayatId === (adminUser ? adminUser.gramPanchayatId : activeGpId)
  );
  const activeGpWaterSchedules = (
    waterSchedules.filter(
      (w) => w.gramPanchayatId === (adminUser ? adminUser.gramPanchayatId : activeGpId)
    ).length > 0
      ? waterSchedules.filter(
          (w) => w.gramPanchayatId === (adminUser ? adminUser.gramPanchayatId : activeGpId)
        )
      : getWaterSchedulesByGpId(adminUser ? adminUser.gramPanchayatId : activeGpId)
  );
  const activeGpNotices = (
    notices.filter(
      (n) => n.gramPanchayatId === (adminUser ? adminUser.gramPanchayatId : activeGpId)
    ).length > 0
      ? notices.filter(
          (n) => n.gramPanchayatId === (adminUser ? adminUser.gramPanchayatId : activeGpId)
        )
      : getNoticesByGpId(adminUser ? adminUser.gramPanchayatId : activeGpId)
  );

  // Admin authentication handlers
  const handleAdminLogin = async (
    idOrMobile: string,
    pass: string,
    otp: string
  ): Promise<{ success: boolean; error?: string }> => {
    const trimmed = idOrMobile.trim();
    const cleanDigits = trimmed.replace(/\D/g, '');

    // Lookup pre-approved officer
    const officer = preapprovedOfficers.find(
      (o) =>
        o.adminId.toLowerCase() === trimmed.toLowerCase() ||
        (cleanDigits.length >= 10 && o.mobileNumber === cleanDigits.slice(-10)) ||
        o.officialEmail.toLowerCase() === trimmed.toLowerCase()
    );

    if (!officer) {
      return {
        success: false,
        error:
          language === 'mr'
            ? 'अधिकारी खाते आढळले नाही किंवा अनधिकृत आहे. जर आपण नवीन अधिकारी असाल तर आधी "अधिकारी खाते सक्रिय करा" निवडा.'
            : 'Officer account not found or unauthorized. If you are a new officer, activate your account first.'
      };
    }

    if (otp !== '852963' && otp !== '123456' && otp !== officer.defaultOtp) {
      return {
        success: false,
        error: language === 'mr' ? 'अवैध OTP! योग्य ६ अंकी OTP प्रविष्ट करा.' : 'Invalid OTP code!'
      };
    }

    // Role-based Gram Panchayat isolation: Officers are linked to EXACTLY ONE Gram Panchayat!
    const authenticatedAdmin: AdminUser = {
      uid: `admin_${officer.mobileNumber}`,
      adminId: officer.adminId,
      email: officer.officialEmail,
      mobileNumber: officer.mobileNumber,
      name: officer.fullName,
      designation: officer.designation,
      role: 'admin',
      active: true,
      districtId: officer.districtId,
      talukaId: officer.talukaId,
      gramPanchayatId: officer.gramPanchayatId,
      gramPanchayatNameMr: officer.gramPanchayatNameMr,
      gramPanchayatNameEn: officer.gramPanchayatNameEn
    };

    setActiveGpId(officer.gramPanchayatId);
    setAdminUser(authenticatedAdmin);
    setCitizenUser(null);
    setCurrentScreen('ADMIN_DASHBOARD');
    return { success: true };
  };

  const handleAdminActivation = async (
    adminIdOrMobile: string,
    otp: string,
    password: string
  ): Promise<{ success: boolean; error?: string; officer?: PreapprovedOfficer }> => {
    const trimmed = adminIdOrMobile.trim();
    const cleanDigits = trimmed.replace(/\D/g, '');

    const officer = preapprovedOfficers.find(
      (o) =>
        o.adminId.toLowerCase() === trimmed.toLowerCase() ||
        (cleanDigits.length >= 10 && o.mobileNumber === cleanDigits.slice(-10)) ||
        o.officialEmail.toLowerCase() === trimmed.toLowerCase()
    );

    if (!officer) {
      return {
        success: false,
        error:
          language === 'mr'
            ? 'अनधिकृत प्रवेश: हा मोबाईल नंबर किंवा आयडी सुपर ॲडमिनने तयार केलेल्या कोणत्याही अधिकारी खात्याशी जुळत नाही. सामान्य नागरिकांना अधिकारी खाते तयार करण्याची परवानगी नाही.'
            : 'Unauthorized: Normal citizens cannot create officer accounts. Contact Super Admin.'
      };
    }

    if (otp !== '852963' && otp !== '123456' && otp !== officer.defaultOtp) {
      return {
        success: false,
        error: language === 'mr' ? 'अवैध OTP कोड!' : 'Invalid OTP code!'
      };
    }

    officer.status = 'ACTIVE';
    officer.active = true;

    // After successful activation, switch to Admin Login
    setCurrentScreen('ADMIN_LOGIN');
    return { success: true, officer };
  };

  // Citizen authentication handlers
  const handleCitizenLogin = async (
    mobile: string,
    pass: string,
    selectedGpId?: string
  ): Promise<{ success: boolean; user?: CitizenUser; error?: string }> => {
    // If Ramesh Patil login (Palaskhed Daulat)
    const isRamesh = mobile === '9876543210';
    const targetGpId = isRamesh ? '172627' : selectedGpId || activeGpId;
    const gp = getGramPanchayatById(targetGpId);
    const taluka = gp ? getTalukaById(gp.talukaId) : undefined;
    const dist = taluka ? getDistrictById(taluka.districtId) : undefined;

    const user: CitizenUser = {
      uid: `citizen_${mobile}`,
      fullName: isRamesh ? 'रमेश सखाराम पाटील' : mobile === '9850112233' ? 'प्रमोद जगताप' : 'नागरिक',
      mobileNumber: mobile,
      aadhaarLastFour: '7890',
      isBpl: false,
      districtId: dist?.id || 'buldhana',
      talukaId: taluka?.id || 'chikhli',
      gramPanchayatId: targetGpId,
      gramPanchayatNameMr: gp?.nameMr || 'आदर्श ग्रामपंचायत पळसखेड दौलत',
      gramPanchayatNameEn: gp?.nameEn || 'Model Grampanchayat Palaskhed Daulat',
      wardNumber: 'प्रभाग १',
      houseNumber: 'घर क्र. १५/अ',
      address: `${gp?.nameMr || ''}, ता. ${taluka?.nameMr || ''}, जि. ${dist?.nameMr || ''}`,
      isRegistered: true
    };

    setActiveGpId(targetGpId);
    setCitizenUser(user);
    setAdminUser(null);
    setCurrentScreen('CITIZEN_HOME');
    return { success: true, user };
  };

  const handleCitizenRegister = async (
    data: Omit<CitizenUser, 'uid' | 'isRegistered'>,
    pass: string
  ): Promise<{ success: boolean; user?: CitizenUser; error?: string }> => {
    const user: CitizenUser = {
      ...data,
      uid: `citizen_${data.mobileNumber}`,
      isRegistered: true
    };

    setActiveGpId(data.gramPanchayatId);
    setCitizenUser(user);
    setAdminUser(null);
    setCurrentScreen('CITIZEN_HOME');
    return { success: true, user };
  };

  // Operations
  const handleUpdateComplaintStatus = (
    id: string,
    status: Complaint['status'],
    remarks: string
  ) => {
    setComplaints((prev) =>
      prev.map((c) =>
        c.id === id
          ? {
              ...c,
              status,
              officerRemarks: remarks,
              resolvedAt:
                status === 'RESOLVED' ? new Date().toLocaleDateString('mr-IN') : c.resolvedAt,
              assignedOfficer: adminUser
                ? `${adminUser.name} (${adminUser.designation})`
                : c.assignedOfficer
            }
          : c
      )
    );
  };

  const handleAddNotice = (notice: Omit<Notice, 'id'>) => {
    const newNotice: Notice = {
      ...notice,
      id: `not-${Date.now()}`,
      gramPanchayatId: adminUser ? adminUser.gramPanchayatId : activeGpId
    };
    setNotices((prev) => [newNotice, ...prev]);
  };

  const handleSubmitComplaint = (complaint: Omit<Complaint, 'id' | 'submittedAt'>) => {
    const newComp: Complaint = {
      ...complaint,
      gramPanchayatId: citizenUser ? citizenUser.gramPanchayatId : activeGpId,
      id: `CMP-2026-0${complaints.length + 4}`,
      submittedAt: new Date().toLocaleDateString('mr-IN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      })
    };
    setComplaints((prev) => [newComp, ...prev]);
  };

  const handleSelectGpContext = (gpId: string) => {
    setActiveGpId(gpId);
    if (citizenUser) {
      const gp = getGramPanchayatById(gpId);
      const taluka = gp ? getTalukaById(gp.talukaId) : undefined;
      const dist = taluka ? getDistrictById(taluka.districtId) : undefined;
      setCitizenUser({
        ...citizenUser,
        gramPanchayatId: gpId,
        gramPanchayatNameMr: gp?.nameMr,
        gramPanchayatNameEn: gp?.nameEn,
        talukaId: taluka?.id || citizenUser.talukaId,
        districtId: dist?.id || citizenUser.districtId
      });
    }
  };

  return (
    <div className="min-h-screen bg-stone-50 flex flex-col font-sans">
      <Header
        language={language}
        onToggleLanguage={toggleLanguage}
        adminUser={adminUser}
        citizenUser={citizenUser}
        currentScreen={currentScreen}
        onNavigate={setCurrentScreen}
        onLogout={handleLogout}
        currentProfile={currentProfile}
        onOpenGpSelector={() => setIsGpChangeModalOpen(true)}
      />

      {/* Modal to change Gram Panchayat context */}
      <GpChangeModal
        language={language}
        isOpen={isGpChangeModalOpen}
        onClose={() => setIsGpChangeModalOpen(false)}
        currentGpId={activeGpId}
        onSelectGpContext={handleSelectGpContext}
      />

      <main className="flex-1">
        {currentScreen === 'ROLE_SELECTION' && (
          <RoleSelectionScreen
            language={language}
            currentProfile={currentProfile}
            onOpenGpSelector={() => setIsGpChangeModalOpen(true)}
            onSelectCitizen={() => setCurrentScreen('CITIZEN_AUTH')}
            onSelectAdmin={() => setCurrentScreen('ADMIN_LOGIN')}
          />
        )}

        {currentScreen === 'ADMIN_LOGIN' && (
          <AdminLoginScreen
            language={language}
            onLogin={handleAdminLogin}
            onNavigateToActivation={() => setCurrentScreen('ADMIN_ACTIVATION')}
            onBackToRoleSelection={() => setCurrentScreen('ROLE_SELECTION')}
          />
        )}

        {currentScreen === 'ADMIN_ACTIVATION' && (
          <AdminActivationScreen
            language={language}
            onActivate={handleAdminActivation}
            onBackToLogin={() => setCurrentScreen('ADMIN_LOGIN')}
          />
        )}

        {currentScreen === 'ADMIN_DASHBOARD' && adminUser && (
          <AdminDashboardScreen
            language={language}
            adminUser={adminUser}
            profile={currentProfile}
            complaints={activeGpComplaints}
            waterSchedules={activeGpWaterSchedules}
            notices={activeGpNotices}
            onUpdateComplaintStatus={handleUpdateComplaintStatus}
            onAddNotice={handleAddNotice}
            onLogout={handleLogout}
          />
        )}

        {currentScreen === 'CITIZEN_AUTH' && (
          <CitizenAuthScreen
            language={language}
            currentGpId={activeGpId}
            onLogin={handleCitizenLogin}
            onRegister={handleCitizenRegister}
            onBackToRoleSelection={() => setCurrentScreen('ROLE_SELECTION')}
          />
        )}

        {currentScreen === 'CITIZEN_HOME' && citizenUser && (
          <CitizenHomeScreen
            language={language}
            citizen={citizenUser}
            profile={currentProfile}
            notices={activeGpNotices}
            complaints={activeGpComplaints}
            waterSchedules={activeGpWaterSchedules}
            onNavigate={setCurrentScreen}
          />
        )}

        {currentScreen === 'CITIZEN_COMPLAINTS' && citizenUser && (
          <CitizenComplaintsScreen
            language={language}
            citizen={citizenUser}
            complaints={activeGpComplaints}
            onSubmitComplaint={handleSubmitComplaint}
          />
        )}

        {currentScreen === 'CITIZEN_WATER' && citizenUser && (
          <CitizenWaterScreen
            language={language}
            citizen={citizenUser}
            waterSchedules={activeGpWaterSchedules}
          />
        )}

        {currentScreen === 'CITIZEN_SERVICES' && citizenUser && (
          <CitizenServicesScreen language={language} citizen={citizenUser} />
        )}

        {currentScreen === 'CITIZEN_NOTICES' && (
          <CitizenNoticesScreen language={language} notices={activeGpNotices} />
        )}

        {currentScreen === 'CITIZEN_INFO' && (
          <CitizenInfoScreen language={language} profile={currentProfile} />
        )}

        {currentScreen === 'CITIZEN_AI_ASSISTANT' && (
          <AiAssistantScreen language={language} />
        )}
      </main>
    </div>
  );
};

export default App;
