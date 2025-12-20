namespace BrasilBurger.ViewModels
{
    public class CommandeValidationModel
    {
        public int CommandeId { get; set; }
        public string TypeLivraison { get; set; } = string.Empty;
        public string AdresseLivraison { get; set; } = string.Empty;
        public string Telephone { get; set; } = string.Empty;
        public string ModePaiement { get; set; } = string.Empty;
        public decimal Total { get; set; }
        public string? Instructions { get; set; }
    }
}