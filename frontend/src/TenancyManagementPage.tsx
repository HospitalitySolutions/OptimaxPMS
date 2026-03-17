import { FormEvent, useEffect, useMemo, useState } from "react";
import axios from "axios";

type TenancyLevel = "APPLICATION" | "ORGANIZATION" | "ACCOUNT" | "PROPERTY";

type TenancyDto = {
  id: number;
  code: string;
  description: string;
  level: TenancyLevel;
  parentId: number | null;
};

const levelLabels: Record<TenancyLevel, string> = {
  APPLICATION: "Application",
  ORGANIZATION: "Organization",
  ACCOUNT: "Account",
  PROPERTY: "Property",
};

export function TenancyManagementPage() {
  const [items, setItems] = useState<TenancyDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [filterLevel, setFilterLevel] = useState<TenancyLevel | "ALL">("ALL");

  const [code, setCode] = useState("");
  const [description, setDescription] = useState("");
  const [level, setLevel] = useState<TenancyLevel>("ORGANIZATION");
  const [parentId, setParentId] = useState<string>("");
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    const load = async () => {
      try {
        setLoading(true);
        const res = await axios.get<TenancyDto[] | unknown>(
          "http://localhost:8080/api/admin/tenancies"
        );
        const data = Array.isArray(res.data) ? (res.data as TenancyDto[]) : [];
        setItems(data);
      } catch (e) {
        setError("Failed to load tenancies.");
      } finally {
        setLoading(false);
      }
    };
    load();
  }, []);

  const filtered = useMemo<TenancyDto[]>(() => {
    if (!Array.isArray(items)) {
      return [];
    }
    if (filterLevel === "ALL") return items;
    return items.filter((i) => i.level === filterLevel);
  }, [items, filterLevel]);

  const onCreate = async (e: FormEvent) => {
    e.preventDefault();
    setSaving(true);
    setError(null);
    try {
      const res = await axios.post<TenancyDto>(
        "http://localhost:8080/api/admin/tenancies",
        {
          code,
          description,
          level,
          parentId: parentId ? Number(parentId) : null,
        }
      );
      setItems((prev) => [...prev, res.data]);
      setCode("");
      setDescription("");
      setParentId("");
    } catch (err: unknown) {
      const msg = axios.isAxiosError(err) && err.response?.data?.error
        ? String(err.response.data.error)
        : "Failed to create tenancy. Check code, description and parent.";
      setError(msg);
    } finally {
      setSaving(false);
    }
  };

  const onDelete = async (id: number) => {
    if (!window.confirm("Delete this tenancy?")) return;
    try {
      await axios.delete(`http://localhost:8080/api/admin/tenancies/${id}`);
      setItems((prev) => prev.filter((i) => i.id !== id));
    } catch {
      setError("Failed to delete tenancy (might have children or dependencies).");
    }
  };

  return (
    <div className="tenancy-page">
      <div className="tenancy-header">
        <div>
          <h2>Tenancy Management</h2>
          <p>Manage Applications, Organizations, Accounts and Properties.</p>
        </div>

        <div className="tenancy-filters">
          <label>
            Level
            <select
              value={filterLevel}
              onChange={(e) => setFilterLevel(e.target.value as TenancyLevel | "ALL")}
            >
              <option value="ALL">All</option>
              <option value="APPLICATION">Application</option>
              <option value="ORGANIZATION">Organization</option>
              <option value="ACCOUNT">Account</option>
              <option value="PROPERTY">Property</option>
            </select>
          </label>
        </div>
      </div>

      <div className="tenancy-layout">
        <section className="tenancy-form-card">
          <h3>Create tenancy node</h3>
          <form onSubmit={onCreate} className="tenancy-form">
            <label>
              Code
              <input
                type="text"
                value={code}
                onChange={(e) => setCode(e.target.value)}
                required
              />
            </label>

            <label>
              Description
              <input
                type="text"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                required
              />
            </label>

            <label>
              Level
              <select
                value={level}
                onChange={(e) => setLevel(e.target.value as TenancyLevel)}
              >
                <option value="ORGANIZATION">Organization</option>
                <option value="ACCOUNT">Account</option>
                <option value="PROPERTY">Property</option>
              </select>
            </label>

            <label>
              Parent ID (optional)
              <input
                type="number"
                value={parentId}
                onChange={(e) => setParentId(e.target.value)}
                placeholder="e.g. 1"
                min={1}
              />
            </label>

            <button className="auth-button" type="submit" disabled={saving}>
              {saving ? "Saving..." : "Create"}
            </button>
          </form>
        </section>

        <section className="tenancy-table-card">
          <h3>Tenancy tree</h3>
          {loading ? (
            <p>Loading...</p>
          ) : (
            <table className="tenancy-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Code</th>
                  <th>Description</th>
                  <th>Level</th>
                  <th>Parent ID</th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {filtered.map((t) => (
                  <tr key={t.id}>
                    <td>{t.id}</td>
                    <td>{t.code}</td>
                    <td>{t.description}</td>
                    <td>{levelLabels[t.level]}</td>
                    <td>{t.parentId ?? "—"}</td>
                    <td>
                      <button
                        type="button"
                        className="secondary-button"
                        onClick={() => onDelete(t.id)}
                      >
                        Delete
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}

          {error && <div className="auth-error" style={{ marginTop: 12 }}>{error}</div>}
        </section>
      </div>
    </div>
  );
}

