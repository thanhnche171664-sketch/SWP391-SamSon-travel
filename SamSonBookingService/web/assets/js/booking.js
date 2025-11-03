// Booking UI micro-interactions & payment polling helpers

export function initParallax() {
  const blob = document.querySelector('.parallax-hero .blob');
  if (!blob) return;
  window.addEventListener('mousemove', (e) => {
    const x = (e.clientX / window.innerWidth - .5) * 20;
    const y = (e.clientY / window.innerHeight - .5) * 20;
    blob.style.transform = `translate3d(${x}%, ${y}%, 0)`;
  });
}

export function startPaymentPolling(endpoint, onPaid) {
  let delay = 1500;
  const maxDelay = 6000;
  const tick = async () => {
    try {
      const res = await fetch(endpoint, { headers: { 'Accept': 'application/json' } });
      const data = await res.json();
      if (data.status === 'paid') {
        onPaid?.();
        return;
      }
    } catch (_) {}
    delay = Math.min(maxDelay, delay + 400);
    setTimeout(tick, delay);
  };
  setTimeout(tick, delay);
}

export function confettiOnce() {
  const el = document.createElement('div');
  el.style.position = 'fixed';
  el.style.inset = '0';
  el.style.pointerEvents = 'none';
  el.innerHTML = '<div style="position:absolute;inset:0;animation:fade 1.6s forwards">🎉</div>';
  document.body.appendChild(el);
  setTimeout(()=> el.remove(), 1600);
}


export function enableCopyButtons() {
  document.querySelectorAll('[data-copy]')?.forEach(btn => {
    btn.addEventListener('click', async () => {
      const sel = btn.getAttribute('data-copy');
      const el = sel ? document.querySelector(sel) : null;
      const text = el ? (el.textContent || '').trim() : '';
      if (!text) return;
      try { await navigator.clipboard.writeText(text); btn.classList.add('copied'); setTimeout(()=>btn.classList.remove('copied'), 1000); } catch {}
    });
  });
}

export function enableImageDownload(selector) {
  document.querySelectorAll(`[data-download]`)?.forEach(btn => {
    btn.addEventListener('click', async () => {
      const sel = btn.getAttribute('data-download');
      const img = sel ? document.querySelector(sel) : null;
      if (!(img instanceof HTMLImageElement) || !img.src) return;
      const a = document.createElement('a');
      a.href = img.src;
      a.download = 'vietqr.png';
      document.body.appendChild(a);
      a.click();
      a.remove();
    });
  });
}


