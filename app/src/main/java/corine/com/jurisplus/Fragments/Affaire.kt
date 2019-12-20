package corine.com.jurisplus.Fragments

class Affaire {
    var code_affaire: String? = null
    var code_droit: String? = null
    var categorie_affaire: String? = null
    var instrumentsL: String? = null
    var code_juridiction: String? = null
    var date_debut: String? = null
    var dateFin: String? = null

    constructor() {}
    constructor(code_affaire: String?, code_droit: String?, categorie_affaire: String?, instrumentsL: String?, code_juridiction: String?, date_debut: String?, dateFin: String?) {
        this.code_affaire = code_affaire
        this.code_droit = code_droit
        this.categorie_affaire = categorie_affaire
        this.instrumentsL = instrumentsL
        this.code_juridiction = code_juridiction
        this.date_debut = date_debut
        this.dateFin = dateFin
    }

}