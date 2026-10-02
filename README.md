# Grampanchayat Citizen & Officer Portal (Palaskhed Daulat)

A comprehensive digital e-governance and citizen portal built with React, Vite, TypeScript, and Tailwind CSS for **Palaskhed Daulat Gram Panchayat** (Taluka: Chikhli, District: Buldhana, Maharashtra).

## Key Features

### 1. Dual-Role Authentication Architecture
- **Role Selection Screen**: Clear, authoritative entry point separating Citizen access from restricted Admin/Officer access.
- **Citizen Portal**:
  - Citizen login & self-registration with ward selection, address, and BPL status.
  - Public grievance redressal (submit issues with location/category, track status).
  - Ward-wise drinking water schedule & instant water tanker booking.
  - Online certificate requests (Birth, Death, Residence, BPL, Water NOC, Construction NOC).
  - Village notices and Gram Sabha announcements.
  - Gram-Mitra AI Assistant (24/7 bilingual guidance in Marathi and English).
- **Admin / Officer Portal**:
  - Dedicated login with Admin ID or registered mobile number + Password + OTP verification.
  - **Restricted Officer Activation Flow**: Officer accounts cannot be created publicly; they must be pre-approved by the Super Admin in the official directory. Entering an unauthorized number or normal citizen mobile number is strictly blocked with a security warning.
  - Role-based and Gram Panchayat data isolation: displays and manages data specifically for **Palaskhed Daulat Gram Panchayat**.
  - Grievance resolution management with official remarks.
  - Notice board publishing and category filtering.
  - 🤖 **AI Automated Voice Call Broadcasting System**: Broadcasts urgent announcements (Gram Sabha, water repairs, tax schemes) to registered village households.

## Getting Started

```bash
# Start development server
npm run dev

# Build for production
npm run build
```
