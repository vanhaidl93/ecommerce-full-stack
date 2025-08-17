import { RenderMode, ServerRoute } from '@angular/ssr';

export const serverAppRoutes: ServerRoute[] = [
  // Routing is order-sensitive -  first match wins.
  {
    path: 'admin/**', // disable SSR for admin pages
    renderMode: RenderMode.Client,
  },
  {
    path: '**',
    renderMode: RenderMode.Prerender, // SSR for the remain routes
  },
];
