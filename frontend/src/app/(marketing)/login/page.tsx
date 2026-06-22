"use client";

import { useRouter } from "next/navigation";
import { useState } from "react";
import { SectionHeading } from "@/components/site";
import { Button, Card, Input } from "@/components/ui";
import { useAuth } from "@/contexts/auth-context";

export default function LoginPage() {
  const router = useRouter();
  const { login } = useAuth();
  const [number, setNumber] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  return (
    <main className="hero" style={{ paddingTop: "1.25rem" }}>
      <div className="hero-grid" style={{ gridTemplateColumns: "minmax(0, 0.8fr) minmax(320px, 0.7fr)" }}>
        <section className="hero-copy">
          {/*<SectionHeading eyebrow="Authentication" title="Sign in to your Nemra workspace" description="Use the backend login endpoint and land directly in the dashboard." />*/}
          <div className="hero-actions">
            <Button variant="secondary" onClick={() => router.push("/register")}>
              Create account
            </Button>
            <Button variant="ghost" onClick={() => router.push("/")}>
              Back home
            </Button>
          </div>
        </section>

        <Card className="input-shell">
          <form
            className="form-grid"
            onSubmit={async (event) => {
              event.preventDefault();
              setError("");
              setLoading(true);
              try {
                await login({ number, password });
                router.push("/dashboard");
              } catch (cause) {
                setError(cause instanceof Error ? cause.message : "Login failed");
              } finally {
                setLoading(false);
              }
            }}
          >
            <div className="field-stack">
              <label className="field-label" htmlFor="number">Phone number</label>
              <Input id="number" value={number} onChange={(e) => setNumber(e.target.value)} placeholder="06..." required />
            </div>
            <div className="field-stack">
              <label className="field-label" htmlFor="password">Password</label>
              <Input id="password" type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="••••••••" required />
            </div>
            {error ? <p className="hint" style={{ color: "#ff9e9e" }}>{error}</p> : null}
            <Button type="submit" disabled={loading}>
              {loading ? "Signing in..." : "Sign in"}
            </Button>
          </form>
        </Card>
      </div>
    </main>
  );
}
