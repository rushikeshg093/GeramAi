import React, { useState } from 'react';
import { AppLanguage, AiChatMessage } from '../types';
import { Sparkles, Send, User, Bot, HelpCircle } from 'lucide-react';

interface AiAssistantScreenProps {
  language: AppLanguage;
}

export const AiAssistantScreen: React.FC<AiAssistantScreenProps> = ({ language }) => {
  const [messages, setMessages] = useState<AiChatMessage[]>([
    {
      id: 'msg-1',
      sender: 'assistant',
      text:
        language === 'mr'
          ? 'नमस्कार! मी पळसखेड दौलत ग्रामपंचायतीचा AI ग्राममित्र आहे. जन्म दाखला, घरपट्टी, पाणीपुरवठा किंवा शासकीय योजनांबद्दल मला प्रश्न विचारा.'
          : 'Hello! I am your AI Gram-Mitra for Palaskhed Daulat Grampanchayat. Ask me any question about village certificates, water schedules, or government schemes.',
      timestamp: '१०:००'
    }
  ]);

  const [input, setInput] = useState('');
  const [isTyping, setIsTyping] = useState(false);

  const handleSend = (userQuestion?: string) => {
    const q = (userQuestion || input).trim();
    if (!q) return;

    const userMsg: AiChatMessage = {
      id: `usr-${Date.now()}`,
      sender: 'user',
      text: q,
      timestamp: new Date().toLocaleTimeString('mr-IN', { hour: '2-digit', minute: '2-digit' })
    };

    setMessages((prev) => [...prev, userMsg]);
    setInput('');
    setIsTyping(true);

    setTimeout(() => {
      let botResponse = '';
      const lower = q.toLowerCase();

      if (lower.includes('दाखला') || lower.includes('certificate') || lower.includes('जन्म') || lower.includes('रहिवासी')) {
        botResponse =
          language === 'mr'
            ? 'पळसखेड दौलत ग्रामपंचायतीकडून जन्म, मृत्यू, रहिवासी व BPL दाखले ऑनलाइन उपलब्ध आहेत. यासाठी "ऑनलाइन दाखले" पर्यायामध्ये जाऊन अर्ज सादर करा. अर्ज मंजूर झाल्यावर २४ ते ७२ तासांत डिजिटल स्वाक्षरी असलेला दाखला थेट डाऊनलोड करता येईल.'
            : 'Birth, death, residence, and BPL certificates are available digitally. Go to "Online Certificates" tab and apply with your Aadhaar and Ration card details.';
      } else if (lower.includes('पाणी') || lower.includes('water') || lower.includes('टँकर') || lower.includes('वेळ')) {
        botResponse =
          language === 'mr'
            ? 'पळसखेड दौलत गावातील प्रभाग १ ते ४ मध्ये सकाळी व सायंकाळी नियमित पाणीपुरवठा केला जातो. जलशुद्धीकरण पूर्ण झाले आहे. टंचाई असल्यास आपण ॲपवरून मोफत टँकर मागणी नोंदवू शकता.'
            : 'Water supply runs morning and evening for all 4 wards. You can book an emergency drinking water tanker from the "Water Services" section.';
      } else if (lower.includes('तक्रार') || lower.includes('गटार') || lower.includes('लाईट') || lower.includes('complaint')) {
        botResponse =
          language === 'mr'
            ? 'नागरी समस्यांसाठी "तक्रार निवारण मंच" मध्ये जाऊन फोटो व सविस्तर पत्त्यासह तक्रार नोंदवा. आपले ग्रामसेवक व संबंधित अधिकारी तात्काळ कारवाई करून शेरा नोंदवतील.'
            : 'You can submit issues regarding streetlights, pipelines, or drainage in the "Grievance Redressal" tab. Officers will review and update status.';
      } else {
        botResponse =
          language === 'mr'
            ? `आपल्या "${q}" या प्रश्नासाठी पळसखेड दौलत ग्रामपंचायत कार्यालयात प्रत्यक्ष संपर्क साधू शकता (वेळ: सकाळी १० ते सायं ५:४५) किंवा सरपंच व ग्रामसेवकांशी फोनवर बोलू शकता.`
            : `For "${q}", please visit Palaskhed Daulat Grampanchayat office during 10 AM to 5:45 PM or connect with the Sarpanch/Gramsevak directly.`;
      }

      setMessages((prev) => [
        ...prev,
        {
          id: `bot-${Date.now()}`,
          sender: 'assistant',
          text: botResponse,
          timestamp: new Date().toLocaleTimeString('mr-IN', { hour: '2-digit', minute: '2-digit' })
        }
      ]);
      setIsTyping(false);
    }, 900);
  };

  return (
    <div className="max-w-3xl mx-auto px-4 py-8">
      <div className="bg-white rounded-2xl border border-stone-200 shadow-sm overflow-hidden flex flex-col h-[75vh]">
        {/* Header */}
        <div className="p-4 border-b border-stone-200 bg-amber-500 text-white flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-white/20 backdrop-blur flex items-center justify-center">
            <Sparkles className="w-5 h-5 text-amber-100" />
          </div>
          <div>
            <h2 className="text-base font-bold">
              {language === 'mr' ? 'ग्राममित्र AI सहाय्यक (Palaskhed Daulat)' : 'Gram-Mitra AI Assistant'}
            </h2>
            <p className="text-xs text-amber-100">
              {language === 'mr' ? 'मराठी व इंग्रजी भाषेमध्ये २४/७ मदत' : '24/7 bilingual village helper'}
            </p>
          </div>
        </div>

        {/* Messages List */}
        <div className="flex-1 p-4 overflow-y-auto space-y-4 bg-stone-50/50">
          {messages.map((m) => (
            <div
              key={m.id}
              className={`flex items-start gap-2.5 ${m.sender === 'user' ? 'justify-end' : 'justify-start'}`}
            >
              {m.sender === 'assistant' && (
                <div className="w-7 h-7 rounded-lg bg-amber-500 text-white flex items-center justify-center shrink-0 text-xs font-bold">
                  🤖
                </div>
              )}
              <div
                className={`max-w-[80%] rounded-2xl p-3.5 text-xs sm:text-sm leading-relaxed shadow-2xs ${
                  m.sender === 'user'
                    ? 'bg-orange-600 text-white rounded-tr-xs'
                    : 'bg-white text-stone-800 border border-stone-200 rounded-tl-xs'
                }`}
              >
                {m.text}
                <div
                  className={`text-[10px] mt-1 text-right ${
                    m.sender === 'user' ? 'text-orange-200' : 'text-stone-400'
                  }`}
                >
                  {m.timestamp}
                </div>
              </div>
              {m.sender === 'user' && (
                <div className="w-7 h-7 rounded-lg bg-stone-700 text-white flex items-center justify-center shrink-0 text-xs">
                  <User className="w-4 h-4" />
                </div>
              )}
            </div>
          ))}

          {isTyping && (
            <div className="flex items-center gap-2 text-xs text-stone-400 italic">
              <span className="animate-bounce">●</span>
              <span className="animate-bounce delay-100">●</span>
              <span className="animate-bounce delay-200">●</span>
              <span>{language === 'mr' ? 'ग्राममित्र विचार करत आहे...' : 'Gram-Mitra is thinking...'}</span>
            </div>
          )}
        </div>

        {/* Quick prompt suggestions */}
        <div className="px-4 py-2 bg-white border-t border-stone-100 flex items-center gap-2 overflow-x-auto text-xs">
          <button
            type="button"
            onClick={() => handleSend(language === 'mr' ? 'रहिवासी दाखला कसा काढायचा?' : 'How to get residence certificate?')}
            className="px-2.5 py-1 rounded-full bg-stone-100 hover:bg-stone-200 text-stone-700 whitespace-nowrap"
          >
            📜 {language === 'mr' ? 'रहिवासी दाखला कसा काढायचा?' : 'Residence Certificate?'}
          </button>
          <button
            type="button"
            onClick={() => handleSend(language === 'mr' ? 'पाणीपुरवठ्याची वेळ काय आहे?' : 'What is water supply timing?')}
            className="px-2.5 py-1 rounded-full bg-stone-100 hover:bg-stone-200 text-stone-700 whitespace-nowrap"
          >
            💧 {language === 'mr' ? 'पाणीपुरवठा वेळ?' : 'Water Timing?'}
          </button>
          <button
            type="button"
            onClick={() => handleSend(language === 'mr' ? 'तक्रार कशी नोंदवायची?' : 'How to submit a grievance?')}
            className="px-2.5 py-1 rounded-full bg-stone-100 hover:bg-stone-200 text-stone-700 whitespace-nowrap"
          >
            ⚠️ {language === 'mr' ? 'तक्रार नोंदणी' : 'Submit grievance?'}
          </button>
        </div>

        {/* Input Bar */}
        <div className="p-3 border-t border-stone-200 bg-white">
          <form
            onSubmit={(e) => {
              e.preventDefault();
              handleSend();
            }}
            className="flex items-center gap-2"
          >
            <input
              type="text"
              value={input}
              onChange={(e) => setInput(e.target.value)}
              placeholder={
                language === 'mr' ? 'येथे आपला प्रश्न विचारा...' : 'Ask your question here...'
              }
              className="flex-1 px-4 py-2.5 text-sm rounded-xl border border-stone-300 focus:outline-none focus:border-amber-500"
            />
            <button
              type="submit"
              disabled={!input.trim()}
              className="p-2.5 rounded-xl bg-orange-600 hover:bg-orange-700 disabled:opacity-40 text-white transition shrink-0"
            >
              <Send className="w-4 h-4" />
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};
