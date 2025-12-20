namespace BrasilBurger.Services
{
    public class Panier
    {
        public List<ItemPanier> Items { get; set; } = new List<ItemPanier>();
        public decimal Total => Items.Sum(item => item.SousTotal);
        
        public class ItemPanier
        {
            public int ProduitId { get; set; }
            public string Type { get; set; } = string.Empty; // "Burger", "Menu", "Complement"
            public string Nom { get; set; } = string.Empty;
            public decimal Prix { get; set; }
            public int Quantite { get; set; }
            public decimal SousTotal => Prix * Quantite;
        }
        
        public void AjouterItem(int produitId, string type, string nom, decimal prix, int quantite = 1)
        {
            var item = Items.FirstOrDefault(i => i.ProduitId == produitId && i.Type == type);
            if (item != null)
            {
                item.Quantite += quantite;
            }
            else
            {
                Items.Add(new ItemPanier
                {
                    ProduitId = produitId,
                    Type = type,
                    Nom = nom,
                    Prix = prix,
                    Quantite = quantite
                });
            }
        }
        
        public void RetirerItem(int produitId, string type)
        {
            var item = Items.FirstOrDefault(i => i.ProduitId == produitId && i.Type == type);
            if (item != null)
            {
                Items.Remove(item);
            }
        }
        
        public void Vider()
        {
            Items.Clear();
        }
    }
}