using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace BrasilBurger.Models
{
    [Table("commandes")]
    public class Commande
    {
        [Key]
        [Column("id")]
        public int Id { get; set; }
       
        [Required]
        [Column("client_id")]
        [ForeignKey("Client")]
        public int ClientId { get; set; }
        public Client? Client { get; set; }
       
        [Column("etat")]
        public EtatCommande Etat { get; set; } = EtatCommande.EN_ATTENTE; // ← Utilisez l'enum
       
        [Required]
        [Column("type_livraison")]
        [StringLength(20)]
        public required string TypeLivraison { get; set; }
       
        [Column("date_commande")]
        public DateTime DateCommande { get; set; } = DateTime.UtcNow;
       
        [Column("date_livraison")]
        public DateTime? DateLivraison { get; set; }
       
        [Column("zone_id")]
        [ForeignKey("Zone")]
        public int? ZoneId { get; set; }
        public Zone? Zone { get; set; }
       
        [Column("livreur_id")]
        [ForeignKey("Livreur")]
        public int? LivreurId { get; set; }
        public Livreur? Livreur { get; set; }
       
        [Column("adresse_livraison")]
        public string? AdresseLivraison { get; set; }
       
        [Required]
        [Column("total")]
        [Range(0, double.MaxValue, ErrorMessage = "Le total ne peut pas être négatif")]
        public decimal Total { get; set; }
       
        [Column("payee")]
        public bool Payee { get; set; } = false;
       
        [Column("archived")]
        public bool Archived { get; set; } = false;
       
        [Column("created_at")]
        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
       
        [Column("updated_at")]
        public DateTime UpdatedAt { get; set; } = DateTime.UtcNow;

        public ICollection<CommandeItem> CommandeItems { get; set; } = new List<CommandeItem>();
        public ICollection<Paiement> Paiements { get; set; } = new List<Paiement>();
    }
}