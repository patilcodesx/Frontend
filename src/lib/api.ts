// export const API_BASE = "https://securevalute-production.up.railway.app";

// export async function apiFetch(path: string, options: any = {}) {
//   const token = localStorage.getItem("securevault_token");

//   const res = await fetch(`https://securevalute-production.up.railway.app${path}`, {
//     ...options,
//     headers: {
//       "Content-Type": "application/json",
//       Authorization: token ? `Bearer ${token}` : "",
//       ...(options.headers || {})
//     }
//   });

//   if (res.status === 401) {
//     throw new Error("Unauthorized (401)");
//   }

//   return await res.json();
// }


export const API_BASE = "https://securevalute-production.up.railway.app";

export async function apiFetch(path: string, options: any = {}) {
  const token = localStorage.getItem("securevault_token");

  const res = await fetch(`https://securevalute-production.up.railway.app${path}`, {
    ...options,
    headers: {
      "Content-Type": "application/json",
      Authorization: token ? `Bearer ${token}` : "",
      ...(options.headers || {})
    }
  });

  // ---- FIX: Handle login errors safely ---- //
  if (!res.ok) {
    let data = null;
    try {
      data = await res.json(); // backend message read karna important!
    } catch {}

    return {
      success: false,
      status: res.status,
      message: data?.message || "Request failed"
    };
  }

  // ---- SUCCESS ---- //
  return await res.json();
}
