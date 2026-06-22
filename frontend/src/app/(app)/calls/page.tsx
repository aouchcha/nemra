"use client";

import { useState } from "react";
import { SectionHeading } from "@/components/site";
import { Button, Card, Input } from "@/components/ui";
import { useAuth } from "@/contexts/auth-context";
import type { CallResponse } from "@/lib/types";

export default function CallsPage() {
  const { request } = useAuth();
  const [clientId, setClientId] = useState("");
  const [providerId, setProviderId] = useState("");
  const [result, setResult] = useState<CallResponse | null>(null);
  const [error, setError] = useState("");

  return (
    <div className="page-grid">
      <SectionHeading eyebrow="LiveKit" title="Generate a call token" description="This hits POST /api/calls and returns the room token plus the LiveKit server URL." />

      <Card className="input-shell">
        <div className="form-2">
          <div className="field-stack">
            <label className="field-label" htmlFor="clientId">Client profile id</label>
            <Input id="clientId" value={clientId} onChange={(e) => setClientId(e.target.value)} />
          </div>
          <div className="field-stack">
            <label className="field-label" htmlFor="providerId">Provider profile id</label>
            <Input id="providerId" value={providerId} onChange={(e) => setProviderId(e.target.value)} />
          </div>
        </div>
        <Button
          onClick={async () => {
            setError("");
            setResult(null);
            try {
              const data = await request<CallResponse>("/api/calls", {
                method: "POST",
                body: { clientId, providerId },
              });
              setResult(data);
            } catch (cause) {
              setError(cause instanceof Error ? cause.message : "Failed to create call");
            }
          }}
        >
          Create room token
        </Button>
        {error ? <p className="hint" style={{ color: "#ff9e9e" }}>{error}</p> : null}
      </Card>

      {result ? (
        <Card className="stack-card">
          <p className="card-title">Token generated</p>
          <p className="muted">URL: {result.url}</p>
          <p className="muted break-words">Token: {result.token}</p>
        </Card>
      ) : null}
    </div>
  );
}
