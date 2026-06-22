"use client";

import { useEffect, useState } from "react";
import { CategoryCard, EmptyState, SectionHeading } from "@/components/site";
import { Button, Card, Input } from "@/components/ui";
import { useAuth } from "@/contexts/auth-context";
import type { CategoryDTO, CreateCategoryRequest } from "@/lib/types";

export default function AdminCategoriesPage() {
  const { request } = useAuth();
  const [categories, setCategories] = useState<CategoryDTO[]>([]);
  const fields = [
    { key: "nameEn", label: "English name" },
    { key: "nameFr", label: "French name" },
    { key: "nameAr", label: "Arabic name" },
  ] as const;
  const [form, setForm] = useState<CreateCategoryRequest>({ nameAr: "", nameFr: "", nameEn: "" });
  const [editingId, setEditingId] = useState<string | null>(null);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  async function reload() {
    try {
      const data = await request<CategoryDTO[]>("/api/categories");
      setCategories(data);
    } catch (cause) {
      setError(cause instanceof Error ? cause.message : "Failed to load categories");
    }
  }

  useEffect(() => {
    void reload();
  }, []);

  return (
    <div className="page-grid">
      <SectionHeading eyebrow="Admin" title="Manage categories" description="Writes go to POST, PUT, and DELETE /api/categories/{id}." />

      <Card className="input-shell">
        <div className="form-2">
          {fields.map(({ key, label }) => (
            <div className="field-stack" key={key}>
              <label className="field-label" htmlFor={key}>{label}</label>
              <Input id={key} value={form[key]} onChange={(e) => setForm((current) => ({ ...current, [key]: e.target.value }))} />
            </div>
          ))}
        </div>
        <div className="button-row">
          <Button
            onClick={async () => {
              try {
                await request(editingId ? `/api/categories/${editingId}` : "/api/categories", {
                  method: editingId ? "PUT" : "POST",
                  body: form,
                });
                setError("");
                setMessage(editingId ? "Category updated." : "Category created.");
                setEditingId(null);
                setForm({ nameAr: "", nameFr: "", nameEn: "" });
                await reload();
              } catch (cause) {
                setError(cause instanceof Error ? cause.message : "Failed to save category");
              }
            }}
          >
            {editingId ? "Update category" : "Create category"}
          </Button>
          {editingId ? (
            <Button variant="secondary" onClick={() => { setEditingId(null); setForm({ nameAr: "", nameFr: "", nameEn: "" }); }}>
              Cancel edit
            </Button>
          ) : null}
        </div>
        {message ? <p className="hint">{message}</p> : null}
        {error ? <p className="hint" style={{ color: "#ff9e9e" }}>{error}</p> : null}
      </Card>

      <Card className="stack-card">
        <SectionHeading eyebrow="Existing" title="Current categories" />
        {categories.length ? categories.map((category) => (
          <div key={category.id} className="stack-card">
            <CategoryCard category={category} />
            <div className="button-row">
              <Button
                variant="secondary"
                onClick={() => {
                  setEditingId(category.id);
                  setForm({
                    nameAr: category.nameAr,
                    nameFr: category.nameFr,
                    nameEn: category.nameEn,
                  });
                }}
              >
                Edit
              </Button>
              <Button
                variant="danger"
                onClick={async () => {
                  await request(`/api/categories/${category.id}`, { method: "DELETE" });
                  await reload();
                }}
              >
                Delete
              </Button>
            </div>
          </div>
        )) : <EmptyState title="No categories returned" />}
      </Card>
    </div>
  );
}
