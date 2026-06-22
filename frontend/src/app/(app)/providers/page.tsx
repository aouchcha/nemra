"use client";

import { useEffect, useState } from "react";
import { EmptyState, ProviderCard, SectionHeading } from "@/components/site";
import { Button, Card, Input, Select } from "@/components/ui";
import { useAuth } from "@/contexts/auth-context";
import type { CategoryDTO, ProviderSummaryDTO } from "@/lib/types";

export default function ProvidersPage() {
  const { request } = useAuth();
  const [providers, setProviders] = useState<ProviderSummaryDTO[]>([]);
  const [categories, setCategories] = useState<CategoryDTO[]>([]);
  const [query, setQuery] = useState("");
  const [category, setCategory] = useState("all");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    async function load() {
      try {
        const [providerData, categoryData] = await Promise.all([
          request<ProviderSummaryDTO[]>("/api/providers"),
          request<CategoryDTO[]>("/api/categories"),
        ]);
        setProviders(providerData);
        setCategories(categoryData);
      } catch (cause) {
        setError(cause instanceof Error ? cause.message : "Failed to load providers");
      } finally {
        setLoading(false);
      }
    }
    void load();
  }, [request]);

  const filtered = providers.filter((provider) => {
    const matchesQuery =
      provider.businessName.toLowerCase().includes(query.toLowerCase()) ||
      provider.fullName.toLowerCase().includes(query.toLowerCase()) ||
      provider.category?.nameEn?.toLowerCase().includes(query.toLowerCase());
    const matchesCategory = category === "all" || provider.category?.nameEn === category;
    return matchesQuery && matchesCategory;
  });

  return (
    <div className="page-grid">
      <SectionHeading eyebrow="Directory" title="Browse providers" description="Search the provider list returned by GET /api/providers." />

      <Card className="input-shell">
        <div className="search-bar">
          <Input value={query} onChange={(e) => setQuery(e.target.value)} placeholder="Search by name or category" />
          <Select value={category} onChange={(e) => setCategory(e.target.value)}>
            <option value="all">All categories</option>
            {categories.map((item) => (
              <option key={item.id} value={item.nameEn}>
                {item.nameEn}
              </option>
            ))}
          </Select>
        </div>
        <div className="button-row">
          <Button variant="secondary" onClick={() => setQuery("")}>Reset search</Button>
        </div>
      </Card>

      {error ? <Card className="stack-card"><p className="card-title">Failed to load</p><p className="muted">{error}</p></Card> : null}
      {loading ? <p className="muted">Loading...</p> : filtered.length ? <div className="grid-2">{filtered.map((provider) => <ProviderCard key={provider.id} provider={provider} />)}</div> : <EmptyState title="No providers match your filters" />}
    </div>
  );
}
