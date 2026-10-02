import React, { useState } from 'react';
import { AppLanguage, CitizenUser, Complaint } from '../types';
import { AlertCircle, Plus, CheckCircle, Clock, Send, Camera } from 'lucide-react';

interface CitizenComplaintsScreenProps {
  language: AppLanguage;
  citizen: CitizenUser;
  complaints: Complaint[];
  onSubmitComplaint: (complaint: Omit<Complaint, 'id' | 'submittedAt'>) => void;
}

export const CitizenComplaintsScreen: React.FC<CitizenComplaintsScreenProps> = ({
  language,
  citizen,
  complaints,
  onSubmitComplaint
}) => {
  const [showNewForm, setShowNewForm] = useState(false);
  const [category, setCategory] = useState('पाणीपुरवठा (Water Supply)');
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [priority, setPriority] = useState<Complaint['priority']>('NORMAL');
  const [successMessage, setSuccessMessage] = useState<string | null>(null);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!title.trim() || !description.trim()) return;

    onSubmitComplaint({
      gramPanchayatId: citizen.gramPanchayatId,
      citizenName: citizen.fullName,
      citizenPhone: citizen.mobileNumber,
      wardNumber: citizen.wardNumber,
      category,
      title: title.trim(),
      description: description.trim(),
      priority,
      status: 'SUBMITTED'
    });

    setTitle('');
    setDescription('');
    setShowNewForm(false);
    setSuccessMessage(
      language === 'mr'
        ? 'आपली तक्रार यशस्वीरीत्या नोंदवली गेली! ग्रामपंचायत अधिकाऱ्यांद्वारे लवकरच कार्यवाही केली जाईल.'
        : 'Complaint submitted successfully! Grampanchayat officers will take action shortly.'
    );
    setTimeout(() => setSuccessMessage(null), 4000);
  };

  return (
    <div className="max-w-4xl mx-auto px-4 py-8">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-6">
        <div>
          <h1 className="text-2xl font-bold text-stone-900">
            {language === 'mr' ? 'नागरिक तक्रार निवारण कक्ष' : 'Citizen Grievance Redressal'}
          </h1>
          <p className="text-xs text-stone-500 mt-1">
            {language === 'mr'
              ? 'रस्ते, वीज, पाणी, स्वच्छता व इतर नागरी समस्यांची तक्रार नोंदवा'
              : 'Report village issues: water supply, roads, sanitation and streetlights'}
          </p>
        </div>

        <button
          onClick={() => setShowNewForm(!showNewForm)}
          className="px-4 py-2.5 rounded-xl bg-orange-600 hover:bg-orange-700 text-white text-xs font-bold shadow-xs transition flex items-center gap-1.5 self-start sm:self-auto"
        >
          <Plus className="w-4 h-4" />
          <span>{showNewForm ? (language === 'mr' ? 'फॉर्म बंद करा' : 'Close Form') : (language === 'mr' ? 'नवीन तक्रार नोंदवा' : 'New Complaint')}</span>
        </button>
      </div>

      {successMessage && (
        <div className="mb-6 p-4 rounded-xl bg-emerald-50 border border-emerald-200 text-xs font-semibold text-emerald-800">
          ✓ {successMessage}
        </div>
      )}

      {/* New Complaint Form */}
      {showNewForm && (
        <div className="bg-white rounded-2xl border border-stone-200 p-6 mb-8 shadow-sm">
          <h2 className="text-lg font-bold text-stone-900 mb-4">
            {language === 'mr' ? 'नवीन तक्रार अर्ज भरा' : 'File a New Grievance'}
          </h2>

          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="grid sm:grid-cols-2 gap-4">
              <div>
                <label className="block text-xs font-bold text-stone-700 mb-1">
                  {language === 'mr' ? 'तक्रारीचा प्रकार (Category) *' : 'Category *'}
                </label>
                <select
                  value={category}
                  onChange={(e) => setCategory(e.target.value)}
                  className="w-full px-3.5 py-2.5 text-sm rounded-xl border border-stone-300 bg-white"
                >
                  <option value="पाणीपुरवठा (Water Supply)">पाणीपुरवठा (Water Supply)</option>
                  <option value="स्वच्छता व कचरा (Sanitation)">स्वच्छता व कचरा (Sanitation)</option>
                  <option value="पथदिवे व वीज (Street Lights)">पथदिवे व वीज (Street Lights)</option>
                  <option value="रस्ते व गटारे (Roads & Drainage)">रस्ते व गटारे (Roads & Drainage)</option>
                  <option value="इतर समस्या (Others)">इतर समस्या (Others)</option>
                </select>
              </div>

              <div>
                <label className="block text-xs font-bold text-stone-700 mb-1">
                  {language === 'mr' ? 'प्राधान्यता (Priority)' : 'Priority'}
                </label>
                <select
                  value={priority}
                  onChange={(e) => setPriority(e.target.value as Complaint['priority'])}
                  className="w-full px-3.5 py-2.5 text-sm rounded-xl border border-stone-300 bg-white"
                >
                  <option value="NORMAL">सामान्य (Normal)</option>
                  <option value="HIGH">महत्त्वाची (High)</option>
                  <option value="URGENT">तातडीची (Urgent)</option>
                </select>
              </div>
            </div>

            <div>
              <label className="block text-xs font-bold text-stone-700 mb-1">
                {language === 'mr' ? 'तक्रारीचा विषय (Title) *' : 'Title *'}
              </label>
              <input
                type="text"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                placeholder="उदा. प्रभाग क्र. २ मधील मुख्य रस्त्यावरील पथदिवा बंद आहे"
                className="w-full px-3.5 py-2.5 text-sm rounded-xl border border-stone-300 focus:outline-none focus:border-orange-500"
                required
              />
            </div>

            <div>
              <label className="block text-xs font-bold text-stone-700 mb-1">
                {language === 'mr' ? 'तक्रारीचा सविस्तर तपशील व ठिकाण *' : 'Detailed Description & Location *'}
              </label>
              <textarea
                rows={4}
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                placeholder="समस्येचे ठिकाण, किती दिवसांपासून समस्या आहे याबद्दल सविस्तर लिहा..."
                className="w-full px-3.5 py-2.5 text-sm rounded-xl border border-stone-300 focus:outline-none focus:border-orange-500"
                required
              />
            </div>

            <div className="flex justify-end gap-2 pt-2">
              <button
                type="button"
                onClick={() => setShowNewForm(false)}
                className="px-4 py-2.5 rounded-xl text-xs font-semibold text-stone-600 hover:bg-stone-100"
              >
                {language === 'mr' ? 'रद्द करा' : 'Cancel'}
              </button>
              <button
                type="submit"
                className="px-5 py-2.5 rounded-xl bg-orange-600 hover:bg-orange-700 text-white text-xs font-bold shadow-xs transition flex items-center gap-1.5"
              >
                <Send className="w-3.5 h-3.5" />
                <span>{language === 'mr' ? 'तक्रार नोंदवा' : 'Submit Complaint'}</span>
              </button>
            </div>
          </form>
        </div>
      )}

      {/* Complaints List */}
      <div className="space-y-4">
        {complaints.map((comp) => (
          <div key={comp.id} className="bg-white rounded-2xl border border-stone-200 p-5 shadow-xs">
            <div className="flex flex-wrap items-center justify-between gap-2 mb-2">
              <div className="flex items-center gap-2">
                <span className="font-mono text-xs font-bold text-stone-500">{comp.id}</span>
                <span className="px-2.5 py-0.5 rounded-full text-[10px] font-bold bg-orange-100 text-orange-800">
                  {comp.category}
                </span>
                <span className="px-2 py-0.5 rounded text-[10px] font-bold bg-stone-100 text-stone-600">
                  {comp.wardNumber}
                </span>
              </div>
              <span
                className={`px-2.5 py-0.5 rounded-full text-xs font-bold ${
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
                  : 'नोंदणी झाली (Submitted)'}
              </span>
            </div>

            <h3 className="text-base font-bold text-stone-900">{comp.title}</h3>
            <p className="text-xs text-stone-600 mt-1 leading-relaxed">{comp.description}</p>

            <div className="mt-3 pt-3 border-t border-stone-100 flex flex-wrap items-center justify-between text-xs text-stone-400 gap-2">
              <span>नागरिक: {comp.citizenName} ({comp.citizenPhone})</span>
              <span>नोंदणी दिनांक: {comp.submittedAt}</span>
            </div>

            {comp.officerRemarks && (
              <div className="mt-3 p-3 rounded-xl bg-orange-50 border border-orange-200 text-xs">
                <div className="font-bold text-orange-950 flex items-center gap-1.5 mb-0.5">
                  <CheckCircle className="w-3.5 h-3.5 text-orange-600" />
                  <span>{language === 'mr' ? 'अधिकाऱ्याचा अधिकृत शेरा:' : 'Officer Remarks:'}</span>
                </div>
                <p className="text-orange-900 leading-relaxed">{comp.officerRemarks}</p>
                {comp.assignedOfficer && (
                  <div className="text-[11px] text-orange-700 mt-1">— {comp.assignedOfficer}</div>
                )}
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};
