namespace BrasilBurger.ViewModels
{
    public class PaiementViewModel
    {
        public int CommandeId { get; set; }
        public decimal Montant { get; set; }
        public string Methode { get; set; } = string.Empty;
        public string? Reference { get; set; }
        
        // Ajoutez ces propriétés pour compatibilité
        public string? NumeroWave { get; set; }
        public string? NumeroOM { get; set; }
    }

    public class PaiementConfirmationViewModel
    {
        public int PaiementId { get; set; }
        public int CommandeId { get; set; }
        public decimal Montant { get; set; }
        public string Methode { get; set; } = string.Empty;
        public DateTime DatePaiement { get; set; }
        public string? Reference { get; set; }
        
        // Ajoutez ces propriétés pour compatibilité
        public CommandeViewModel? Commande { get; set; }
    }
}