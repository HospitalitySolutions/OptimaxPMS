import { NavLink, Route, Routes, useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";
import { TenancyManagementPage } from "../TenancyManagementPage";

function Placeholder({ title }: { title: string }) {
  return (
    <div className="page-placeholder">
      <h2>{title}</h2>
      <p>Administration module scaffolded. Configure tenancy and settings here.</p>
    </div>
  );
}

export function AdminLayout() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  return (
    <div className="app-shell">
      <aside className="app-sidebar">
        <div className="app-logo">Optimax PMS</div>
        <nav className="app-nav">
          <NavLink to="tenancy">Tenancy Management</NavLink>
          <NavLink to="rooms">Room Management</NavLink>
          <NavLink to="rates">Rate Management</NavLink>
          <NavLink to="guests">Guest Management</NavLink>
          <NavLink to="users">User Management</NavLink>
          <NavLink to="housekeeping">Housekeeping Management</NavLink>
          <NavLink to="settings">Application Settings</NavLink>
        </nav>
      </aside>

      <div className="app-main">
        <header className="app-header">
          <div className="app-header-left">
            <h1>Administration</h1>
          </div>
          <div className="app-header-right">
            <button
              className="mode-switch"
              type="button"
              onClick={() => navigate("/operations")}
            >
              Switch to Operations
            </button>
            <span className="user-pill">{user?.email}</span>
            <button className="secondary-button" onClick={logout}>
              Logout
            </button>
          </div>
        </header>

        <main className="app-content">
          <Routes>
            <Route path="/" element={<Placeholder title="Administration dashboard" />} />
            <Route path="tenancy" element={<TenancyManagementPage />} />
            <Route path="rooms" element={<Placeholder title="Room Management" />} />
            <Route path="rates" element={<Placeholder title="Rate Management" />} />
            <Route path="guests" element={<Placeholder title="Guest Management" />} />
            <Route path="users" element={<Placeholder title="User Management" />} />
            <Route path="housekeeping" element={<Placeholder title="Housekeeping Management" />} />
            <Route path="settings" element={<Placeholder title="Application Settings" />} />
          </Routes>
        </main>
      </div>
    </div>
  );
}

