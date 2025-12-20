using Microsoft.EntityFrameworkCore;
using BrasilBurger.Models;

namespace BrasilBurger.Data
{
    public class ApplicationDbContext : DbContext
    {
        public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options)
            : base(options)
        {
        }

        public DbSet<Client> Clients { get; set; } = null!;
        public DbSet<Burger> Burgers { get; set; } = null!;
        public DbSet<Menu> Menus { get; set; } = null!;
        public DbSet<Complement> Complements { get; set; } = null!;
        public DbSet<Commande> Commandes { get; set; } = null!;
        public DbSet<CommandeItem> CommandeItems { get; set; } = null!;
        public DbSet<Paiement> Paiements { get; set; } = null!;
        public DbSet<Zone> Zones { get; set; } = null!;
        public DbSet<Livreur> Livreurs { get; set; } = null!;

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);
            modelBuilder.Entity<Commande>()
                .Property(e => e.Etat)
                .HasConversion<string>()
                .HasColumnName("etat")
                .HasMaxLength(20);

            // ✅ CORRECTION 1: Désactiver les conventions problématiques
            modelBuilder.Entity<Menu>()
                .Property<int?>("FriteId")
                .HasColumnName("frite_id");
            
            modelBuilder.Entity<Menu>()
                .Property<int?>("BoissonId")
                .HasColumnName("boisson_id");

            // ✅ CORRECTION 2: Supprimer les shadow properties problématiques
            foreach (var entity in modelBuilder.Model.GetEntityTypes())
            {
                // Supprimer toutes les colonnes en Id1 créées par EF
                var propertiesToRemove = entity.GetProperties()
                    .Where(p => p.Name.EndsWith("Id1") || 
                                p.Name.EndsWith("Id2") ||
                                p.Name.EndsWith("Id3"))
                    .ToList();
                
                foreach (var property in propertiesToRemove)
                {
                    entity.RemoveProperty(property.Name);
                }
            }

            // ✅ CORRECTION 3: Configuration MANUELLE des relations Menu-Complement
            modelBuilder.Entity<Menu>()
                .HasOne(m => m.Frite)
                .WithMany() // Pas de navigation inverse ici
                .HasForeignKey(m => m.FriteId)
                .HasConstraintName("FK_Menu_Frite")
                .IsRequired(false)
                .OnDelete(DeleteBehavior.Restrict);

            modelBuilder.Entity<Menu>()
                .HasOne(m => m.Boisson)
                .WithMany() // Pas de navigation inverse ici
                .HasForeignKey(m => m.BoissonId)
                .HasConstraintName("FK_Menu_Boisson")
                .IsRequired(false)
                .OnDelete(DeleteBehavior.Restrict);

            // ✅ CORRECTION 4: Configuration CommandeItem-Complement
            modelBuilder.Entity<CommandeItem>()
                .HasOne(ci => ci.Complement)
                .WithMany(c => c.CommandeItems)
                .HasForeignKey(ci => ci.ComplementId)
                .HasConstraintName("FK_CommandeItem_Complement")
                .IsRequired(false)
                .OnDelete(DeleteBehavior.Restrict);

            // ✅ CORRECTION 5: Relation Complement-Menu (optionnel)
            modelBuilder.Entity<Complement>()
                .HasMany(c => c.MenusAsFrite)
                .WithOne(m => m.Frite)
                .HasForeignKey(m => m.FriteId)
                .IsRequired(false)
                .OnDelete(DeleteBehavior.Restrict);

            modelBuilder.Entity<Complement>()
                .HasMany(c => c.MenusAsBoisson)
                .WithOne(m => m.Boisson)
                .HasForeignKey(m => m.BoissonId)
                .IsRequired(false)
                .OnDelete(DeleteBehavior.Restrict);

            // ✅ Configuration des autres relations
            modelBuilder.Entity<Menu>()
                .HasOne(m => m.Burger)
                .WithMany(b => b.Menus)
                .HasForeignKey(m => m.BurgerId)
                .OnDelete(DeleteBehavior.Restrict);

            modelBuilder.Entity<CommandeItem>()
                .HasOne(ci => ci.Burger)
                .WithMany(b => b.CommandeItems)
                .HasForeignKey(ci => ci.BurgerId)
                .OnDelete(DeleteBehavior.Restrict);

            modelBuilder.Entity<CommandeItem>()
                .HasOne(ci => ci.Menu)
                .WithMany(m => m.CommandeItems)
                .HasForeignKey(ci => ci.MenuId)
                .OnDelete(DeleteBehavior.Restrict);

            // ✅ Check constraint pour CommandeItem
            modelBuilder.Entity<CommandeItem>(entity =>
{
    entity.ToTable(t => t.HasCheckConstraint(
        "CK_CommandeItem_Type",
        @"(
            (burger_id IS NOT NULL AND menu_id IS NULL AND complement_id IS NULL)
         OR (burger_id IS NULL AND menu_id IS NOT NULL AND complement_id IS NULL)
         OR (burger_id IS NULL AND menu_id IS NULL AND complement_id IS NOT NULL)
        )"));
});

            // ✅ Configuration du schéma public pour toutes les tables
            modelBuilder.Entity<Burger>().ToTable("burgers", "public");
            modelBuilder.Entity<Client>().ToTable("clients", "public");
            modelBuilder.Entity<Menu>().ToTable("menus", "public");
            modelBuilder.Entity<Complement>().ToTable("complements", "public");
            modelBuilder.Entity<Commande>().ToTable("commandes", "public");
            modelBuilder.Entity<CommandeItem>().ToTable("commande_items", "public");
            modelBuilder.Entity<Paiement>().ToTable("paiements", "public");
            modelBuilder.Entity<Zone>().ToTable("zones", "public");
            modelBuilder.Entity<Livreur>().ToTable("livreurs", "public");
        }
    }
}