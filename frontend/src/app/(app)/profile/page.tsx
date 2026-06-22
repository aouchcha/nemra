"use client";

import { useEffect, useState } from "react";
import { SectionHeading } from "@/components/site";
import { Button, Card, Input, Textarea } from "@/components/ui";
import { useAuth } from "@/contexts/auth-context";
import type { UserDTO } from "@/lib/types";

export default function ProfilePage() {
  const { user, request, updateMe } = useAuth();
  const [profile, setProfile] = useState<UserDTO | null>(null);
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
    business_name: "",
    category: "",
    bio: "",
    years_of_experience: "",
  });
  const [feedback, setFeedback] = useState("");

  useEffect(() => {
    async function load() {
      try {
        const me = await request<UserDTO>("/api/users/me");
        setProfile(me);
        setForm((current) => ({
          ...current,
          fullName: me.fullName,
          phoneNumber: me.phone,
          city: me.city,
        }));
      } catch (cause) {
        setFeedback(cause instanceof Error ? cause.message : "Failed to load profile");
      }
    }
    void load();
  }, [request]);

  return (
    <div className="page-grid">
      <SectionHeading eyebrow="Profile" title="Manage your account" description="The backend exposes PUT /api/users/me and, for providers, PUT /api/providers/me." />

      <Card className="input-shell">
        <div className="form-2">
          {fields.map(({ key, label }) => (
            <div className="field-stack" key={key}>
              <label className="field-label" htmlFor={key}>{label}</label>
              <Input id={key} value={form[key]} onChange={(e) => setForm((current) => ({ ...current, [key]: e.target.value }))} />
            </div>
          ))}
          <div className="field-stack">
            <label className="field-label" htmlFor="password">New password</label>
            <Input id="password" type="password" value={form.password} onChange={(e) => setForm((current) => ({ ...current, password: e.target.value }))} />
          </div>
        </div>

        {profile?.role === "PROVIDER" ? (
          <>
            <p className="muted">
              Provider updates are submitted to <code>/api/providers/me</code>. Fill in the current provider values before saving.
            </p>
            <div className="form-2">
              <div className="field-stack">
                <label className="field-label" htmlFor="business_name">Business name</label>
                <Input id="business_name" value={form.business_name} onChange={(e) => setForm((current) => ({ ...current, business_name: e.target.value }))} />
              </div>
              <div className="field-stack">
                <label className="field-label" htmlFor="category">Category</label>
                <Input id="category" value={form.category} onChange={(e) => setForm((current) => ({ ...current, category: e.target.value }))} />
              </div>
              <div className="field-stack">
                <label className="field-label" htmlFor="years_of_experience">Years of experience</label>
                <Input id="years_of_experience" type="number" min="0" value={form.years_of_experience} onChange={(e) => setForm((current) => ({ ...current, years_of_experience: e.target.value }))} />
              </div>
            </div>
            <div className="field-stack">
              <label className="field-label" htmlFor="bio">Bio</label>
              <Textarea id="bio" value={form.bio} onChange={(e) => setForm((current) => ({ ...current, bio: e.target.value }))} />
            </div>
          </>
        ) : null}

        <Button
          onClick={async () => {
            setFeedback("");
            try {
              await updateMe({
                fullName: form.fullName,
                phoneNumber: form.phoneNumber,
                password: form.password,
                city: form.city,
              });
              if (profile?.role === "PROVIDER") {
                await request("/api/providers/me", {
                  method: "PUT",
                  body: {
                    fullName: form.fullName,
                    phoneNumber: form.phoneNumber,
                    password: form.password,
                    city: form.city,
                    business_name: form.business_name,
                    category: form.category,
                    bio: form.bio,
                    years_of_experience: form.years_of_experience ? Number(form.years_of_experience) : undefined,
                  },
                });
              }
              setFeedback("Profile updated.");
            } catch (cause) {
              setFeedback(cause instanceof Error ? cause.message : "Failed to update profile");
            }
          }}
        >
          Save changes
        </Button>
        {feedback ? <p className="hint">{feedback}</p> : null}
      </Card>

      <Card className="stack-card">
        <SectionHeading eyebrow="Session" title="Account details" />
        <p className="card-title">{profile?.fullName ?? user?.fullName}</p>
        <p className="muted">{profile?.phone ?? user?.phone}</p>
        <p className="muted">{profile?.role ?? user?.role}</p>
      </Card>
    </div>
  );
}
