import React, { useState } from 'react';
import { AppLanguage, CitizenUser, OnlineServiceItem, ServiceApplication } from '../types';
import { defaultOnlineServices } from '../data/maharashtraData';
import { FileText, CheckCircle2, Clock, Download, ArrowRight, ShieldCheck } from 'lucide-react';

interface CitizenServicesScreenProps {
  language: AppLanguage;
  citizen: CitizenUser;
}

export const CitizenServicesScreen: React.FC<CitizenServicesScreenProps> = ({
  language,
  citizen
}) => {
  const [selectedService, setSelectedService] = useState<OnlineServiceItem | null>(null);
  const [applications, setApplications] = useState<ServiceApplication[]>([
    {
      id: 'APP-2026-041',
      gramPanchayatId: citizen.gramPanchayatId,
      serviceId: 'srv-residence',
      serviceTitle: 'रहिवासी दाखला (Residence Certificate)',
      applicantName: citizen.fullName,
      applicantPhone: citizen.mobileNumber,
      wardNumber: citizen.wardNumber,
      appliedDate: '२०२६-०९-२८',
      status: 'APPROVED',
      trackingNumber: 'TRACK-98214',
      certificateUrl: '#'
    }
  ]);
  const [successMsg, setSuccessMsg] = useState<string | null>(null);

  const handleApply = (service: OnlineServiceItem) => {
    const newApp: ServiceApplication = {
      id: `APP-2026-0${applications.length + 42}`,
      gramPanchayatId: citizen.gramPanchayatId,
      serviceId: service.id,
      serviceTitle: service.titleMr,
      applicantName: citizen.fullName,
      applicantPhone: citizen.mobileNumber,
      wardNumber: citizen.wardNumber,
      appliedDate: new Date().toLocaleDateString('mr-IN'),
      status: 'UNDER_REVIEW',
      trackingNumber: `TRACK-${Math.floor(10000 + Math.random() * 90000)}`
    };

    setApplications([newApp, ...applications]);
    setSelectedService(null);
    setSuccessMsg(
      language === 'mr'
        ? `दाखल्यासाठी अर्ज यशस्वीरीत्या सादर केला! ट्रॅकिंग क्र: ${newApp.trackingNumber}`
        : `Application submitted! Tracking No: ${newApp.trackingNumber}`
    );
    setTimeout(() => setSuccessMsg(null), 5000);
  };

  return (
    <div className="max-w-4xl mx-auto px-4 py-8">
      <div className="mb-6">
        <h1 className="text-2xl font-bold text-stone-900">
          {language === 'mr' ? 'ऑनलाइन दाखले व शासकीय सेवा' : 'Online Certificates & Services'}
        </h1>
        <p className="text-xs text-stone-500 mt-1">
          {language === 'mr'
            ? 'पळसखेड दौलत ग्रामपंचायतीचे अधिकृत डिजिटल स्वाक्षरी असलेले दाखले घरबसल्या मिळवा'
            : 'Apply for digitally signed village certificates & track status'}
        </p>
      </div>

      {successMsg && (
        <div className="mb-6 p-4 rounded-xl bg-emerald-50 border border-emerald-200 text-xs font-semibold text-emerald-800">
          ✓ {successMsg}
        </div>
      )}

      {/* Services Grid */}
      <div className="grid md:grid-cols-2 gap-4 mb-8">
        {defaultOnlineServices.map((srv) => (
          <div
            key={srv.id}
            className="bg-white p-5 rounded-2xl border border-stone-200 shadow-xs flex flex-col justify-between"
          >
            <div>
              <div className="flex items-center justify-between text-xs mb-2">
                <span className="font-mono font-bold text-orange-600 bg-orange-50 px-2 py-0.5 rounded">
                  {srv.code}
                </span>
                <span className="text-stone-500 font-semibold">शुल्क: ₹{srv.fee}</span>
              </div>
              <h3 className="text-base font-bold text-stone-900">{srv.titleMr}</h3>
              <p className="text-xs text-stone-600 mt-1 leading-relaxed">{srv.descriptionMr}</p>

              <div className="mt-3 text-[11px] text-stone-500">
                <span className="font-semibold text-stone-700">कागदपत्रे: </span>
                {srv.requiredDocuments.join(', ')}
              </div>
            </div>

            <div className="mt-5 pt-3 border-t border-stone-100 flex items-center justify-between">
              <span className="text-xs text-stone-400">वेळ: {srv.deliveryDays} दिवस</span>
              <button
                type="button"
                onClick={() => setSelectedService(srv)}
                className="px-3.5 py-1.5 rounded-lg bg-orange-600 hover:bg-orange-700 text-white text-xs font-bold transition flex items-center gap-1"
              >
                <span>{language === 'mr' ? 'अर्ज करा' : 'Apply'}</span>
                <ArrowRight className="w-3.5 h-3.5" />
              </button>
            </div>
          </div>
        ))}
      </div>

      {/* Applications Tracking */}
      <div>
        <h2 className="text-base font-bold text-stone-900 mb-3 flex items-center gap-2">
          <Clock className="w-5 h-5 text-stone-700" />
          <span>{language === 'mr' ? 'माझे सादर केलेले अर्ज व दाखले' : 'My Applications'}</span>
        </h2>

        <div className="space-y-3">
          {applications.map((app) => (
            <div key={app.id} className="bg-white rounded-2xl border border-stone-200 p-5 shadow-xs">
              <div className="flex items-center justify-between mb-1.5">
                <div className="flex items-center gap-2">
                  <span className="font-mono text-xs font-bold text-stone-500">{app.trackingNumber}</span>
                  <span className="text-xs text-stone-400">({app.appliedDate})</span>
                </div>
                <span
                  className={`px-2.5 py-0.5 rounded-full text-xs font-bold ${
                    app.status === 'APPROVED'
                      ? 'bg-emerald-100 text-emerald-800'
                      : 'bg-amber-100 text-amber-800'
                  }`}
                >
                  {app.status === 'APPROVED' ? 'मंजूर (Approved)' : 'तपासणी चालू (Under Review)'}
                </span>
              </div>
              <div className="text-sm font-bold text-stone-900">{app.serviceTitle}</div>
              <div className="text-xs text-stone-500 mt-0.5">अर्जदार: {app.applicantName}</div>

              {app.status === 'APPROVED' && (
                <div className="mt-3 pt-3 border-t border-stone-100 flex items-center justify-between text-xs">
                  <span className="text-emerald-700 font-semibold flex items-center gap-1">
                    <ShieldCheck className="w-4 h-4" />
                    <span>डिजिटल स्वाक्षरी प्रमाणित दाखला तयार आहे</span>
                  </span>
                  <button
                    type="button"
                    onClick={() => alert('दाखला डाऊनलोड झाला: ' + app.serviceTitle)}
                    className="px-3 py-1.5 rounded-lg bg-emerald-600 hover:bg-emerald-700 text-white font-bold flex items-center gap-1 transition"
                  >
                    <Download className="w-3.5 h-3.5" />
                    <span>{language === 'mr' ? 'दाखला डाऊनलोड करा' : 'Download'}</span>
                  </button>
                </div>
              )}
            </div>
          ))}
        </div>
      </div>

      {/* Confirmation Modal */}
      {selectedService && (
        <div className="fixed inset-0 z-50 bg-black/40 backdrop-blur-xs flex items-center justify-center p-4">
          <div className="bg-white rounded-2xl max-w-md w-full p-6 shadow-xl border border-stone-200">
            <h3 className="text-lg font-bold text-stone-900 mb-1">{selectedService.titleMr}</h3>
            <p className="text-xs text-stone-500 mb-4">{selectedService.departmentMr}</p>

            <div className="space-y-3 text-xs bg-stone-50 p-4 rounded-xl border border-stone-200 mb-4">
              <div>अर्जदार: <span className="font-bold text-stone-900">{citizen.fullName}</span></div>
              <div>मोबाईल: <span className="font-bold text-stone-900">{citizen.mobileNumber}</span></div>
              <div>प्रभाग: <span className="font-bold text-stone-900">{citizen.wardNumber}</span></div>
              <div>शासकीय शुल्क: <span className="font-bold text-orange-600">₹{selectedService.fee}</span></div>
            </div>

            <div className="flex justify-end gap-2">
              <button
                type="button"
                onClick={() => setSelectedService(null)}
                className="px-4 py-2 rounded-xl text-xs font-semibold text-stone-600 hover:bg-stone-100"
              >
                रद्द करा
              </button>
              <button
                type="button"
                onClick={() => handleApply(selectedService)}
                className="px-4 py-2 rounded-xl bg-orange-600 hover:bg-orange-700 text-white text-xs font-bold shadow-xs transition"
              >
                अर्ज सादर करा
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
