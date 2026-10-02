package com.example

import com.example.data.local.MaharashtraDirectory
import org.junit.Assert.*
import org.junit.Test

class OfficerAuthTest {

    @Test
    fun testPreapprovedOfficerLookupForPalaskhedDaulat() {
        // Gramsevak / VDO
        val gramsevak = MaharashtraDirectory.findPreapprovedOfficer("9423889900")
        assertNotNull("Gramsevak officer must be pre-approved by Super Admin", gramsevak)
        assertEquals("OFF-BULD-CHK-001", gramsevak?.adminId)
        assertEquals("gp_palaskhed_daulat", gramsevak?.gramPanchayatId)
        assertEquals("buldhana", gramsevak?.districtId)
        assertEquals("chikhli", gramsevak?.talukaId)

        // Lookup by Admin ID
        val sarpanch = MaharashtraDirectory.findPreapprovedOfficer("OFF-BULD-CHK-002")
        assertNotNull("Sarpanch officer must be found by Admin ID", sarpanch)
        assertEquals("9822114455", sarpanch?.mobileNumber)
        assertEquals("gp_palaskhed_daulat", sarpanch?.gramPanchayatId)
    }

    @Test
    fun testNormalCitizenCannotBeFoundInOfficerDirectory() {
        // Normal citizen mobile number
        val citizenMobile = "9876543210"
        val unauthorizedOfficer = MaharashtraDirectory.findPreapprovedOfficer(citizenMobile)
        assertNull("A normal citizen must never be found in the pre-approved officer registry", unauthorizedOfficer)

        val randomMobile = "9111122222"
        val randomOfficer = MaharashtraDirectory.findPreapprovedOfficer(randomMobile)
        assertNull("Arbitrary mobile numbers must not have officer access", randomOfficer)
    }

    @Test
    fun testOfficerGramPanchayatIsolation() {
        val buldhanaOfficer = MaharashtraDirectory.findPreapprovedOfficer("OFF-BULD-CHK-001")
        assertEquals("gp_palaskhed_daulat", buldhanaOfficer?.gramPanchayatId)

        val puneOfficer = MaharashtraDirectory.findPreapprovedOfficer("OFF-PUNE-HAV-001")
        assertEquals("gp_wagholi", puneOfficer?.gramPanchayatId)
        assertNotEquals(buldhanaOfficer?.gramPanchayatId, puneOfficer?.gramPanchayatId)
    }
}
