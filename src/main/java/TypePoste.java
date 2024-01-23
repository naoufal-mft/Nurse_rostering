
public enum TypePoste {
    MATIN("Matin"),
    APRES_MIDI("Après-midi"),
    NUIT("Nuit");

    private final String libelle;

    TypePoste(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
