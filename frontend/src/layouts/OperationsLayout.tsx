import { NavLink, Outlet, Route, Routes, useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

function Placeholder({ title }: { title: string }) {
  return (
    <div className="page-placeholder">
      <h2>{title}</h2>
      <p>Feature module scaffolded. Implement business logic next.</p>
    </div>
  );
}

export function OperationsLayout() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  return (
    <div className="app-shell">
      <aside className="app-sidebar">
        <div className="app-logo">Optimax PMS</div>
        <nav className="app-nav">
          <NavLink to="availability">Availability</NavLink>
          <NavLink to="reservations">Reservations</NavLink>
          <NavLink to="guests">Guests</NavLink>
          <NavLink to="billing">Billing</NavLink>
          <NavLink to="housekeeping">Housekeeping</NavLink>
          <NavLink to="inbox">Inbox</NavLink>
          <NavLink to="analytics">Analytics</NavLink>
          <NavLink to="integrations">Integrations</NavLink>
          <NavLink to="log">Log</NavLink>
        </nav>
      </aside>

      <div className="app-main">
        <header className="app-header">
          <div className="app-header-left">
            <h1>Operations</h1>
          </div>
          <div className="app-header-right">
            <button
              className="mode-switch"
              type="button"
              onClick={() => navigate("/admin")}
            >
              Switch to Administration
            </button>
            <span className="user-pill">{user?.email}</span>
            <button className="secondary-button" onClick={logout}>
              Logout
            </button>
          </div>
        </header>

        <main className="app-content">
          <Routes>
            <Route path="/" element={<Placeholder title="Operations dashboard" />} />
            <Route path="availability" element={<Placeholder title="Availability" />} />
            <Route path="reservations" element={<Placeholder title="Reservations" />} />
            <Route path="guests" element={<Placeholder title="Guests" />} />
            <Route path="billing" element={<Placeholder title="Billing" />} />
            <Route path="housekeeping" element={<Placeholder title="Housekeeping" />} />
            <Route path="inbox" element={<Placeholder title="Inbox" />} />
            <Route path="analytics" element={<Placeholder title="Analytics" />} />
            <Route path="integrations" element={<Placeholder title="Integrations" />} />
            <Route path="log" element={<Placeholder title="Activity log" />} />
          </Routes>
          <Outlet />
        </main>
      </div>
    </div>
  );
}

