namespace BrasilBurger.Models
{
    public enum EtatCommande
    {
        EN_ATTENTE,      // = 0
        VALIDEE,         // = 1
        EN_PREPARATION,  // = 2
        PRETE,           // = 3
        EN_LIVRAISON,    // = 4
        LIVRE,          // = 5 (avec double 'E')
        ANNULÉE,         // = 6 (SANS accent)
        TERMINEE        // = 7
    }
}