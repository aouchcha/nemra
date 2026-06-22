"use client";

import { useEffect, useState } from "react";
import { CategoryCard, EmptyState, SectionHeading } from "@/components/site";
import { Card, Input } from "@/components/ui";
import { useAuth } from "@/contexts/auth-context";
import type { CategoryDTO } from "@/lib/types";

export default function CategoriesPage() {
  const { request } = useAuth();
  const [categories, setCategories] = useState<CategoryDTO[]>([]);
  const [query, setQuery] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function load() {
      try {
        const data = await request<CategoryDTO[]>("/api/categories");
        setCategories(data);
      } catch (cause) {
        setError(cause instanceof Error ? cause.message : "Failed to load categories");
      } finally {
        setLoading(false);
      }
    }
    void load();
  }, [request]);

  const filtered = categories.filter((category) =>
    [category.nameEn, category.nameFr, category.nameAr].some((value) =>
      value?.toLowerCase().includes(query.toLowerCase())
    )
  );

  return (
    <div className="page-grid">
      <SectionHeading eyebrow="Taxonomy" title="Browse categories" description="Read-only view of GET /api/categories, which the backend returns as a raw array." />
      <Card className="input-shell">
        <Input value={query} onChange={(e) => setQuery(e.target.value)} placeholder="Search categories" />
      </Card>
      {error ? <Card className="stack-card"><p className="card-title">Failed to load</p><p className="muted">{error}</p></Card> : null}
      {loading ? <p className="muted">Loading...</p> : filtered.length ? <div className="grid-2">{filtered.map((category) => <CategoryCard key={category.id} category={category} />)}</div> : <EmptyState title="No categories found" />}
    </div>
  );
}
