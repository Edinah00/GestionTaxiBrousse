package mg.coop.servlet.personne;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mg.coop.dao.PersonneDAO;
import mg.coop.model.Personne;

@WebServlet("/personnes")
public class PersonneServlet extends HttpServlet {

    private PersonneDAO dao = new PersonneDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            List<Personne> personnes = dao.findAll();
            request.setAttribute("personnes", personnes);
            request.getRequestDispatcher("personnes.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            String nom = request.getParameter("nom");
            String telephone = request.getParameter("telephone");
            String role = request.getParameter("role");

            Personne p = new Personne();
            p.setNom(nom);
            p.setTelephone(telephone);
            p.setRole(role);

            dao.add(p);
            response.sendRedirect("personnes");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
