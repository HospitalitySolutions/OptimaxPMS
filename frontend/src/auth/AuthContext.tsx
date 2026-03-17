import { createContext, ReactNode, useContext, useEffect, useState } from "react";
import axios from "axios";

type AuthUser = {
  userId: number;
  email: string;
  role: string;
};

type AuthContextValue = {
  user: AuthUser | null;
  token: string | null;
  loading: boolean;
  login: (email: string, password: string) => Promise<void>;
  logout: () => void;
};

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

const STORAGE_KEY = "optimax_auth";

type Props = {
  children: ReactNode;
};

export function AuthProvider({ children }: Props) {
  const [user, setUser] = useState<AuthUser | null>(null);
  const [token, setToken] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const stored = window.localStorage.getItem(STORAGE_KEY);
    if (stored) {
      const parsed = JSON.parse(stored) as { user: AuthUser; token: string };
      setUser(parsed.user);
      setToken(parsed.token);
      axios.defaults.headers.common.Authorization = `Bearer ${parsed.token}`;
    }
    setLoading(false);
  }, []);

  const login = async (email: string, password: string) => {
    const res = await axios.post("http://localhost:8080/api/auth/login", {
      email,
      password,
    });

    const authUser: AuthUser = {
      userId: res.data.userId,
      email: res.data.email,
      role: res.data.role,
    };

    setUser(authUser);
    setToken(res.data.token);
    axios.defaults.headers.common.Authorization = `Bearer ${res.data.token}`;

    window.localStorage.setItem(
      STORAGE_KEY,
      JSON.stringify({ user: authUser, token: res.data.token })
    );
  };

  const logout = () => {
    setUser(null);
    setToken(null);
    delete axios.defaults.headers.common.Authorization;
    window.localStorage.removeItem(STORAGE_KEY);
  };

  return (
    <AuthContext.Provider value={{ user, token, loading, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) {
    throw new Error("useAuth must be used within AuthProvider");
  }
  return ctx;
}

