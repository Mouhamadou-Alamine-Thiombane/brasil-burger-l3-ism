using Microsoft.EntityFrameworkCore;
using BrasilBurger.Data;
using BrasilBurger.Models;

namespace BrasilBurger.Services
{
    public class SessionService
    {
        private readonly ApplicationDbContext _context;
        private readonly IHttpContextAccessor _httpContextAccessor;

        public SessionService(ApplicationDbContext context, IHttpContextAccessor httpContextAccessor)
        {
            _context = context;
            _httpContextAccessor = httpContextAccessor;
        }

        public int? GetClientId()
        {
            return _httpContextAccessor.HttpContext?.Session.GetInt32("ClientId");
        }

        public string? GetClientNom()
        {
            return _httpContextAccessor.HttpContext?.Session.GetString("ClientNom");
        }

        public string? GetClientPrenom()
        {
            return _httpContextAccessor.HttpContext?.Session.GetString("ClientPrenom");
        }

        public string? GetClientEmail()
        {
            return _httpContextAccessor.HttpContext?.Session.GetString("ClientEmail");
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

        public bool IsAuthenticated()
        {
            return GetClientId().HasValue;
        }

        public void SetClientSession(Client client)
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

        public void ClearSession()
        {
            var session = _httpContextAccessor.HttpContext?.Session;
            if (session != null)
            {
                session.Clear();
            }
        }

        public Dictionary<string, object?> GetSessionData()
        {
            var session = _httpContextAccessor.HttpContext?.Session;
            if (session == null)
                return new Dictionary<string, object?>();

            return new Dictionary<string, object?>
            {
                ["ClientId"] = GetClientId(),
                ["ClientNom"] = GetClientNom(),
                ["ClientPrenom"] = GetClientPrenom(),
                ["ClientEmail"] = GetClientEmail(),
                ["IsAuthenticated"] = IsAuthenticated()
            };
        }
    }
}