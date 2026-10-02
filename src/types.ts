export type AppLanguage = 'mr' | 'en';

export interface District {
  id: string;
  nameMr: string;
  nameEn: string;
  code: string;
  division?: string;
}

export interface Taluka {
  id: string;
  code?: string;
  districtId: string;
  nameMr: string;
  nameEn: string;
}

export interface GramPanchayat {
  id: string;
  code?: string;
  talukaId: string;
  districtId: string;
  nameMr: string;
  nameEn: string;
  pincode: string;
}

export interface Village {
  id: string; // official LGD village code
  code: string;
  gramPanchayatId: string; // official LGD GP code
  talukaId: string; // official LGD subdistrict code
  districtId: string; // official LGD district code
  nameMr: string;
  nameEn: string;
}

export interface PreapprovedOfficer {
  adminId: string;
  mobileNumber: string;
  officialEmail: string;
  fullName: string;
  fullNameEn: string;
  designation: string;
  designationEn: string;
  districtId: string;
  talukaId: string;
  gramPanchayatId: string;
  gramPanchayatNameMr: string;
  gramPanchayatNameEn: string;
  activationToken: string;
  defaultOtp: string;
  status: 'PENDING_ACTIVATION' | 'ACTIVE';
  active: boolean;
  role: 'admin' | 'officer';
}

export interface AdminUser {
  uid: string;
  adminId: string;
  email: string;
  mobileNumber: string;
  name: string;
  designation: string;
  role: 'admin' | 'officer';
  active: boolean;
  districtId: string;
  talukaId: string;
  gramPanchayatId: string;
  gramPanchayatNameMr: string;
  gramPanchayatNameEn: string;
}

export interface CitizenUser {
  uid: string;
  fullName: string;
  mobileNumber: string;
  email?: string;
  aadhaarLastFour: string;
  rationCardNumber?: string;
  isBpl: boolean;
  bplNumber?: string;
  districtId: string;
  talukaId: string;
  gramPanchayatId: string;
  gramPanchayatNameMr?: string;
  gramPanchayatNameEn?: string;
  villageId?: string;
  villageNameMr?: string;
  villageNameEn?: string;
  wardNumber: string;
  houseNumber: string;
  address: string;
  isRegistered: boolean;
}

export interface Complaint {
  id: string;
  gramPanchayatId: string;
  citizenName: string;
  citizenPhone: string;
  wardNumber: string;
  category: string;
  title: string;
  description: string;
  imageUrl?: string;
  status: 'SUBMITTED' | 'IN_PROGRESS' | 'RESOLVED' | 'REJECTED';
  priority: 'NORMAL' | 'HIGH' | 'URGENT';
  submittedAt: string;
  resolvedAt?: string;
  officerRemarks?: string;
  assignedOfficer?: string;
}

export interface WaterSchedule {
  id: string;
  gramPanchayatId: string;
  wardNumber: string;
  wardName: string;
  morningTime: string;
  eveningTime: string;
  status: 'ACTIVE' | 'DELAYED' | 'MAINTENANCE';
  nextSupplyDate: string;
  chlorinationDone: boolean;
  tankerAvailable: boolean;
  notes?: string;
}

export interface TankerBooking {
  id: string;
  gramPanchayatId: string;
  citizenName: string;
  citizenPhone: string;
  wardNumber: string;
  deliveryDate: string;
  purpose: string;
  status: 'REQUESTED' | 'APPROVED' | 'DISPATCHED' | 'COMPLETED' | 'CANCELLED';
  requestedAt: string;
  driverName?: string;
  driverPhone?: string;
}

export interface OnlineServiceItem {
  id: string;
  code: string;
  titleMr: string;
  titleEn: string;
  departmentMr: string;
  departmentEn: string;
  descriptionMr: string;
  descriptionEn: string;
  fee: number;
  deliveryDays: number;
  requiredDocuments: string[];
}

export interface ServiceApplication {
  id: string;
  gramPanchayatId: string;
  serviceId: string;
  serviceTitle: string;
  applicantName: string;
  applicantPhone: string;
  wardNumber: string;
  appliedDate: string;
  status: 'UNDER_REVIEW' | 'DOCUMENT_VERIFIED' | 'APPROVED' | 'REJECTED';
  certificateUrl?: string;
  trackingNumber: string;
  rejectionReason?: string;
}

export interface Notice {
  id: string;
  gramPanchayatId: string;
  titleMr: string;
  titleEn: string;
  contentMr: string;
  contentEn: string;
  category: 'GRAMSABHA' | 'WATER' | 'TAX' | 'HEALTH' | 'GENERAL';
  publishedDate: string;
  isImportant: boolean;
  attachmentName?: string;
}

export interface Official {
  id: string;
  name: string;
  roleMr: string;
  roleEn: string;
  phone: string;
  photoUrl?: string;
}

export interface Project {
  id: string;
  titleMr: string;
  titleEn: string;
  budget: string;
  status: 'PLANNED' | 'ONGOING' | 'COMPLETED';
  completionPercent: number;
  scheme: string;
}

export interface PanchayatProfile {
  id: string;
  nameMr: string;
  nameEn: string;
  districtMr: string;
  districtEn: string;
  talukaMr: string;
  talukaEn: string;
  pincode: string;
  population: number;
  households: number;
  wardsCount: number;
  sarpanchName: string;
  sarpanchPhone: string;
  gramsevakName: string;
  gramsevakPhone: string;
  officeAddress: string;
  officeHours: string;
  helplineNumber: string;
}

export interface AiChatMessage {
  id: string;
  sender: 'user' | 'assistant';
  text: string;
  timestamp: string;
}

export type MainRole = 'CITIZEN' | 'ADMIN';
export type ScreenDestination =
  | 'ROLE_SELECTION'
  | 'CITIZEN_AUTH'
  | 'CITIZEN_HOME'
  | 'CITIZEN_COMPLAINTS'
  | 'CITIZEN_WATER'
  | 'CITIZEN_SERVICES'
  | 'CITIZEN_NOTICES'
  | 'CITIZEN_INFO'
  | 'CITIZEN_AI_ASSISTANT'
  | 'CITIZEN_PROFILE'
  | 'ADMIN_LOGIN'
  | 'ADMIN_ACTIVATION'
  | 'ADMIN_DASHBOARD';
