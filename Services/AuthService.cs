using Microsoft.EntityFrameworkCore;
using BrasilBurger.Data;
using BrasilBurger.Models;
using BrasilBurger.ViewModels;

namespace BrasilBurger.Services
{
    public class AuthService
    {
        private readonly ApplicationDbContext _context;
        private readonly IHttpContextAccessor _httpContextAccessor;

        public AuthService(ApplicationDbContext context, IHttpContextAccessor httpContextAccessor)
        {
            _context = context;
            _httpContextAccessor = httpContextAccessor;
        }

        public async Task<Client?> LoginAsync(LoginViewModel model)
        {
            var client = await _context.Clients
                .FirstOrDefaultAsync(c => c.Email == model.Email && c.MotDePasse == model.Password);

            if (client != null)
            {
                SetClientSession(client);
            }

            return client;
        }

        public async Task<(bool Success, string Message)> RegisterAsync(RegisterViewModel model)
        {
            try
            {
                // Check if email already exists
                if (await _context.Clients.AnyAsync(c => c.Email == model.Email))
                {
                    return (false, "Cet email est déjà utilisé");
                }

                // Check if phone already exists
                if (await _context.Clients.AnyAsync(c => c.Telephone == model.Telephone))
                {
                    return (false, "Ce numéro de téléphone est déjà utilisé");
                }

                var client = new Client
                {
                    Nom = model.Nom,
                    Prenom = model.Prenom,
                    Telephone = model.Telephone,
                    Email = model.Email,
                    MotDePasse = model.Password,
                    Adresse = model.Adresse,
                    CreatedAt = DateTime.Now,
                    UpdatedAt = DateTime.Now
                };

                _context.Clients.Add(client);
                await _context.SaveChangesAsync();

                SetClientSession(client);

                return (true, "Inscription réussie");
            }
            catch (Exception ex)
            {
                return (false, $"Erreur lors de l'inscription: {ex.Message}");
            }
        }

        public void Logout()
        {
            ClearClientSession();
        }

        public bool IsAuthenticated()
        {
            return _httpContextAccessor.HttpContext?.Session.GetInt32("ClientId") != null;
        }

        public int? GetClientId()
        {
            return _httpContextAccessor.HttpContext?.Session.GetInt32("ClientId");
        }

        public async Task<Client?> GetCurrentClientAsync()
        {
            var clientId = GetClientId();
            if (clientId.HasValue)
            {
                return await _context.Clients.FindAsync(clientId.Value);
            }
            return null;
        }

        private void SetClientSession(Client client)
        {
            var session = _httpContextAccessor.HttpContext?.Session;
            if (session != null && client != null)
            {
                session.SetInt32("ClientId", client.Id);
                session.SetString("ClientNom", client.Nom);
                session.SetString("ClientPrenom", client.Prenom);
                session.SetString("ClientEmail", client.Email);
            }
        }

        private void ClearClientSession()
        {
            var session = _httpContextAccessor.HttpContext?.Session;
            if (session != null)
            {
                session.Clear();
            }
        }
    }
}