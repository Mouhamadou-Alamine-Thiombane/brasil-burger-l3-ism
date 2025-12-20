using BrasilBurger.Data;
using BrasilBurger.Models;

namespace BrasilBurger.Data
{
    public static class SeedData
    {
        public static async Task Initialize(ApplicationDbContext context)
        {
            if (!context.Clients.Any())
            {
                // Add default admin client
                var admin = new Client
                {
                    Nom = "Admin",
                    Prenom = "System",
                    Telephone = "770000000",
                    Email = "admin@brasilburger.sn",
                    MotDePasse = "admin123",
                    Adresse = "Plateau, Dakar",
                    Archived = false,
                    CreatedAt = DateTime.Now,
                    UpdatedAt = DateTime.Now
                };

                context.Clients.Add(admin);
                await context.SaveChangesAsync();
            }
        }
    }
}