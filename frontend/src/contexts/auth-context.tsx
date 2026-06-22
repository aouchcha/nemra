"use client";

import { createContext, useContext, useEffect, useRef, useState } from "react";
import { apiPath, extractMessage, readJson, unwrapApiData } from "@/lib/api";
import type { ReactNode } from "react";
import type {
  AuthResponse,
  LoginRequest,
  RegisterClient,
  RegisterProvider,
  RegisterRequest,
  UserDTO,
} from "@/lib/types";

type Session = AuthResponse | null;

type RequestInitWithBody = Omit<RequestInit, "body"> & {
  body?: BodyInit | object | null;
};

type AuthContextValue = {
  ready: boolean;
  session: Session;
  user: UserDTO | null;
  isAuthenticated: boolean;
  request: <T>(path: string, init?: RequestInitWithBody) => Promise<T>;
  login: (payload: LoginRequest) => Promise<UserDTO>;
  registerClient: (payload: RegisterClient) => Promise<UserDTO>;
  registerProvider: (payload: RegisterProvider) => Promise<UserDTO>;
  updateMe: (payload: RegisterRequest) => Promise<UserDTO>;
  logout: () => Promise<void>;
  refreshSession: () => Promise<Session>;
};

const STORAGE_KEY = "nemra-auth";

const AuthContext = createContext<AuthContextValue | null>(null);

function toJsonBody(body: RequestInitWithBody["body"]) {
  if (!body || body instanceof FormData || typeof body !== "object" || body instanceof Blob) {
    return body;
  }

  return JSON.stringify(body);
}

function isFormData(body: RequestInitWithBody["body"]) {
  return typeof FormData !== "undefined" && body instanceof FormData;
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const [session, setSession] = useState<Session>(null);
  const [ready, setReady] = useState(false);
  const sessionRef = useRef<Session>(null);

  function persist(next: Session) {
    sessionRef.current = next;
    setSession(next);
    if (typeof window === "undefined") {
      return;
    }
    if (next) {
      window.localStorage.setItem(STORAGE_KEY, JSON.stringify(next));
    } else {
      window.localStorage.removeItem(STORAGE_KEY);
    }
  }

  useEffect(() => {
    const stored = window.localStorage.getItem(STORAGE_KEY);
    if (stored) {
      try {
        const parsed = JSON.parse(stored) as Session;
        sessionRef.current = parsed;
        setSession(parsed);
      } catch {
        window.localStorage.removeItem(STORAGE_KEY);
      }
    }
    setReady(true);
  }, []);

  async function refreshSession() {
    const current = sessionRef.current;
    if (!current?.refreshToken) {
      persist(null);
      return null;
    }

    const response = await fetch(apiPath("/api/auth/refresh-token"), {
      method: "POST",
      headers: {
        Authorization: `Bearer ${current.refreshToken}`,
      },
    });

    if (!response.ok) {
      persist(null);
      return null;
    }

    const payload = unwrapApiData<AuthResponse>(await readJson(response));
    if (payload?.accessToken && payload?.refreshToken && payload?.user) {
      persist(payload);
      return payload;
    }

    persist(null);
    return null;
  }

  async function request<T>(path: string, init: RequestInitWithBody = {}, retry = true): Promise<T> {
    const headers = new Headers(init.headers);
    const current = sessionRef.current;
    const shouldSendJson = init.body && !isFormData(init.body) && !headers.has("Content-Type");

    if (current?.accessToken) {
      headers.set("Authorization", `Bearer ${current.accessToken}`);
    }

    if (shouldSendJson) {
      headers.set("Content-Type", "application/json");
    }

    const response = await fetch(apiPath(path), {
      ...init,
      body: toJsonBody(init.body),
      headers,
    });

    if (response.status === 401 && retry && sessionRef.current?.refreshToken) {
      const refreshed = await refreshSession();
      if (refreshed?.accessToken) {
        return request<T>(path, init, false);
      }
    }

    const payload = await readJson<unknown>(response);
    if (!response.ok) {
      throw new Error(extractMessage(payload, response.statusText || "Request failed"));
    }

    return unwrapApiData<T>(payload);
  }

  async function login(payload: LoginRequest) {
    const data = await request<AuthResponse>("/api/auth/login", {
      method: "POST",
      body: payload,
    });
    persist(data);
    return data.user;
  }

  async function registerClient(payload: RegisterClient) {
    const data = await request<AuthResponse>("/api/auth/register/client", {
      method: "POST",
      body: payload,
    });
    persist(data);
    return data.user;
  }

  async function registerProvider(payload: RegisterProvider) {
    const formData = new FormData();
    formData.append("fullName", payload.fullName);
    formData.append("phoneNumber", payload.phoneNumber);
    formData.append("password", payload.password);
    formData.append("city", payload.city);
    formData.append("business_name", payload.business_name);
    formData.append("category", payload.category);
    if (payload.bio) {
      formData.append("bio", payload.bio);
    }
    if (typeof payload.years_of_experience === "number") {
      formData.append("years_of_experience", String(payload.years_of_experience));
    }
    if (payload.avatar) {
      formData.append("avatar", payload.avatar);
    }

    const data = await request<AuthResponse>("/api/auth/register/provider", {
      method: "POST",
      body: formData,
    });
    persist(data);
    return data.user;
  }

  async function updateMe(payload: RegisterRequest) {
    const data = await request<UserDTO>("/api/users/me", {
      method: "PUT",
      body: payload,
    });
    if (sessionRef.current) {
      const next = { ...sessionRef.current, user: data };
      persist(next);
    }
    return data;
  }

  async function logout() {
    const current = sessionRef.current;
    if (current?.refreshToken) {
      await fetch(apiPath("/api/auth/logout"), {
        method: "POST",
        headers: {
          Authorization: `Bearer ${current.refreshToken}`,
        },
      });
    }
    persist(null);
  }

  const value: AuthContextValue = {
    ready,
    session,
    user: session?.user ?? null,
    isAuthenticated: Boolean(session?.accessToken && session?.refreshToken),
    request,
    login,
    registerClient,
    registerProvider,
    updateMe,
    logout,
    refreshSession,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within AuthProvider");
  }
  return context;
}

export function useOptionalAuth() {
  return useContext(AuthContext);
}
