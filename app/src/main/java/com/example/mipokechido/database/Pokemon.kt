package com.example.mipokechido.database

class Pokemon(PkmnID: Int, PkmnName: String, PkmnHp: String, PkmnAtk: String, PkmnAtkSpc: String, PkmnDef: String, PkmnDefSpc: String, PkmnSpd: String, PkmnSprite: String){
    val PkmnID = PkmnID
    val PkmnName = PkmnName
    val PkmnHp = PkmnHp
    val PkmnAtk = PkmnAtk
    val PkmnAtkSpc = PkmnAtkSpc
    val PkmnDef = PkmnDef
    val PkmnDefSpc = PkmnDefSpc
    val PkmnSpd = PkmnSpd
    val PkmnSprite = PkmnSprite
}

fun Pokemon.toPokemonEntity() = PokemonEntity(
    PkmnID, PkmnName, PkmnHp, PkmnAtk, PkmnAtkSpc, PkmnDef, PkmnDefSpc, PkmnSpd, PkmnSprite
)