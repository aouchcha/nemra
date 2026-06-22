"use client";

import { useRouter } from "next/navigation";
import { useState } from "react";
import { SectionHeading } from "@/components/site";
import { Button, Card, Input } from "@/components/ui";
import { useAuth } from "@/contexts/auth-context";

export default function ClientRegisterPage() {
  const router = useRouter();
  const { registerClient } = useAuth();
  const fields = [
    { key: "fullName", label: "Full name" },
    { key: "phoneNumber", label: "Phone number" },
    { key: "city", label: "City" },
  ] as const;
  const [form, setForm] = useState({
    fullName: "",
    phoneNumber: "",
    password: "",
    city: "",
  });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  return (
    <main className="hero" style={{ paddingTop: "1.25rem" }}>
      <div className="hero-grid">
        {/*<section className="hero-copy">*/}
        {/*  <SectionHeading eyebrow="Client onboarding" title="Create a client account" description="This hits POST /api/auth/register/client and stores the session tokens." />*/}
        {/*</section>*/}

        <Card className="input-shell">
          <form
            className="form-grid"
            onSubmit={async (event) => {
              event.preventDefault();
              setError("");
              setLoading(true);
              try {
                await registerClient(form);
                router.push("/dashboard");
              } catch (cause) {
                setError(cause instanceof Error ? cause.message : "Registration failed");
              } finally {
                setLoading(false);
              }
            }}
          >
            {fields.map(({ key, label }) => (
              <div className="field-stack" key={key}>
                <label className="field-label" htmlFor={key}>{label}</label>
                <Input
                  id={key}
                  value={form[key]}
                  onChange={(e) => setForm((current) => ({ ...current, [key]: e.target.value }))}
                  required
                />
              </div>
            ))}
            <div className="field-stack">
              <label className="field-label" htmlFor="password">Password</label>
              <Input
                id="password"
                type="password"
                value={form.password}
                onChange={(e) => setForm((current) => ({ ...current, password: e.target.value }))}
                required
              />
            </div>
            {error ? <p className="hint" style={{ color: "#ff9e9e" }}>{error}</p> : null}
            <div className="button-row">
              <Button type="submit" disabled={loading}>
                {loading ? "Creating..." : "Create account"}
              </Button>
              <Button type="button" variant="secondary" onClick={() => router.push("/login")}>
                Already have an account
              </Button>
            </div>
          </form>
        </Card>
      </div>
    </main>
  );
}
