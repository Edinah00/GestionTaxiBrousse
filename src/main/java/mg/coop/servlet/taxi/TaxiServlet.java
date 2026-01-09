package mg.coop.servlet.taxi;

import mg.coop.dao.TaxiBrousseDAO;
import mg.coop.model.TaxiBrousse;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class TaxiServlet extends HttpServlet {

    private TaxiBrousseDAO dao = new TaxiBrousseDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

        String action = req.getParameter("action");

        try {
            if ("add".equals(action)) {
                // FORMULAIRE AJOUT, PAS D'ID
                req.getRequestDispatcher("/WEB-INF/jsp/taxi/form.jsp").forward(req, resp);

            } else if ("edit".equals(action)) {
                String idStr = req.getParameter("id");
                if (idStr != null && !idStr.isEmpty()) {
                    int id = Integer.parseInt(idStr);
                    req.setAttribute("taxi", dao.findById(id));
                    req.getRequestDispatcher("/WEB-INF/jsp/taxi/form.jsp").forward(req, resp);
                } else {
                    resp.sendRedirect("taxi"); // aucun ID → retour à la liste
                }

            } else if ("delete".equals(action)) {
                String idStr = req.getParameter("id");
                if (idStr != null && !idStr.isEmpty()) {
                    int id = Integer.parseInt(idStr);

                    // ⚠ Vérifier si taxi est utilisé dans taxi_trajet
                    if (dao.isUsed(id)) {
                        req.setAttribute("error", "Impossible de supprimer : taxi déjà utilisé dans un trajet");
                        req.setAttribute("taxis", dao.findAll());
                        req.getRequestDispatcher("/WEB-INF/jsp/taxi/list.jsp").forward(req, resp);
                    } else {
                        dao.delete(id);
                        resp.sendRedirect("taxi");
                    }
                } else {
                    resp.sendRedirect("taxi");
                }

            } else {
                // LISTE PAR DÉFAUT
                req.setAttribute("taxis", dao.findAll());
                req.getRequestDispatcher("/WEB-INF/jsp/taxi/list.jsp").forward(req, resp);
            }

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            TaxiBrousse t = new TaxiBrousse();
            t.setCooperativeId(Integer.parseInt(req.getParameter("cooperativeId")));
            t.setTypeVoitureId(Integer.parseInt(req.getParameter("typeVoitureId")));
            t.setImmatriculation(req.getParameter("immatriculation"));

            String id = req.getParameter("id");
            if (id == null || id.isEmpty()) {
                dao.insert(t);
            } else {
                t.setId(Integer.parseInt(id));
                dao.update(t);
            }

            resp.sendRedirect("taxi");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
