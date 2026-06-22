import type {
  ButtonHTMLAttributes,
  HTMLAttributes,
  InputHTMLAttributes,
  ReactNode,
  SelectHTMLAttributes,
  TextareaHTMLAttributes,
} from "react";

export function buttonClass(variant: "primary" | "secondary" | "ghost" | "danger" = "primary") {
  const base = "button";
  const variants = {
    primary: "button--primary",
    secondary: "button--secondary",
    ghost: "button--ghost",
    danger: "button--danger",
  };

  return `${base} ${variants[variant]}`;
}

export function Button({
  variant = "primary",
  className = "",
  ...props
}: ButtonHTMLAttributes<HTMLButtonElement> & { variant?: "primary" | "secondary" | "ghost" | "danger" }) {
  return <button className={`${buttonClass(variant)} ${className}`} {...props} />;
}

export function Input({ className = "", ...props }: InputHTMLAttributes<HTMLInputElement>) {
  return (
    <input
      className={`input ${className}`}
      {...props}
    />
  );
}

export function Textarea({ className = "", ...props }: TextareaHTMLAttributes<HTMLTextAreaElement>) {
  return (
    <textarea
      className={`textarea ${className}`}
      {...props}
    />
  );
}

export function Select({ className = "", ...props }: SelectHTMLAttributes<HTMLSelectElement>) {
  return (
    <select
      className={`select ${className}`}
      {...props}
    />
  );
}

export function Card({
  className = "",
  ...props
}: HTMLAttributes<HTMLDivElement>) {
  return <div className={`panel ${className}`} {...props} />;
}

export function Badge({
  tone = "neutral",
  children,
}: {
  tone?: "neutral" | "accent" | "success" | "danger";
  children: ReactNode;
}) {
  const tones = {
    neutral: "badge--neutral",
    accent: "badge--accent",
    success: "badge--success",
    danger: "badge--danger",
  };

  return <span className={`badge ${tones[tone]}`}>{children}</span>;
}
