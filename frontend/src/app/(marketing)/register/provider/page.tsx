"use client";

import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { SectionHeading } from "@/components/site";
import { Button, Card, Input, Select, Textarea } from "@/components/ui";
import { useAuth } from "@/contexts/auth-context";
import type { CategoryDTO } from "@/lib/types";

export default function ProviderRegisterPage() {
  const router = useRouter();
  const { registerProvider, request } = useAuth();
  const fields = [
    { key: "fullName", label: "Full name" },
    { key: "phoneNumber", label: "Phone number" },
    { key: "city", label: "City" },
    { key: "business_name", label: "Business name" },
  ] as const;
  const [categories, setCategories] = useState<CategoryDTO[]>([]);
  const [loadingCategories, setLoadingCategories] = useState(true);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [form, setForm] = useState({
    fullName: "",
    phoneNumber: "",
    password: "",
    city: "",
    business_name: "",
    category: "",
    bio: "",
    years_of_experience: "",
    avatar: null as File | null,
  });

  useEffect(() => {
    async function load() {
      try {
        const data = await request<CategoryDTO[]>("/api/categories");
        setCategories(data);
        if (data[0]?.nameEn) {
          setForm((current) => (current.category ? current : { ...current, category: data[0].nameEn }));
        }
      } catch {
        setCategories([]);
      } finally {
        setLoadingCategories(false);
      }
    }
    void load();
  }, [request]);

  return (
    <main className="hero" style={{ paddingTop: "1.25rem" }}>
      <div className="hero-grid">
        {/*<section className="hero-copy">*/}
        {/*  <SectionHeading eyebrow="Provider onboarding" title="Create a provider account" description="This submits multipart/form-data to POST /api/auth/register/provider." />*/}
        {/*  <p>*/}
        {/*    Categories are loaded from the API when the session allows it. If the backend still requires auth here,*/}
        {/*    the category field falls back to a manual text value.*/}
        {/*  </p>*/}
        {/*</section>*/}

        <Card className="input-shell">
          <form
            className="form-grid"
            onSubmit={async (event) => {
              event.preventDefault();
              setError("");
              setLoading(true);
              try {
                await registerProvider({
                  ...form,
                  years_of_experience: form.years_of_experience ? Number(form.years_of_experience) : undefined,
                });
                router.push("/dashboard");
              } catch (cause) {
                setError(cause instanceof Error ? cause.message : "Registration failed");
              } finally {
                setLoading(false);
              }
            }}
          >
            <div className="form-2">
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
            </div>

            <div className="form-2">
              <div className="field-stack">
                <label className="field-label" htmlFor="category">Category</label>
                {loadingCategories ? (
                  <Input id="category" value={form.category} onChange={(e) => setForm((current) => ({ ...current, category: e.target.value }))} placeholder="Loading categories..." />
                ) : categories.length > 0 ? (
                  <Select id="category" value={form.category} onChange={(e) => setForm((current) => ({ ...current, category: e.target.value }))}>
                    {categories.map((category) => (
                      <option key={category.id} value={category.nameEn}>
                        {category.nameEn}
                      </option>
                    ))}
                  </Select>
                ) : (
                  <Input id="category" value={form.category} onChange={(e) => setForm((current) => ({ ...current, category: e.target.value }))} placeholder="Enter category name" required />
                )}
              </div>
              <div className="field-stack">
                <label className="field-label" htmlFor="years_of_experience">Years of experience</label>
                <Input
                  id="years_of_experience"
                  type="number"
                  min="0"
                  value={form.years_of_experience}
                  onChange={(e) => setForm((current) => ({ ...current, years_of_experience: e.target.value }))}
                />
              </div>
            </div>

            <div className="field-stack">
              <label className="field-label" htmlFor="bio">Bio</label>
              <Textarea
                id="bio"
                value={form.bio}
                onChange={(e) => setForm((current) => ({ ...current, bio: e.target.value }))}
              />
            </div>

            <div className="field-stack">
              <label className="field-label" htmlFor="avatar">Avatar</label>
              <Input
                id="avatar"
                type="file"
                accept="image/*"
                onChange={(e) => setForm((current) => ({ ...current, avatar: e.target.files?.[0] ?? null }))}
              />
            </div>

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
                {loading ? "Creating..." : "Create provider account"}
              </Button>
              <Button type="button" variant="secondary" onClick={() => router.push("/register")}>
                Back
              </Button>
            </div>
          </form>
        </Card>
      </div>
    </main>
  );
}
