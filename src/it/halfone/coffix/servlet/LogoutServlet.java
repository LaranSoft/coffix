package it.halfone.coffix.servlet;

import it.halfone.coffix.constants.Servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * LogoutServlet - 20/ott/2013
 * <br>
 * <h1>RServlet</h1>
 * <br>
 * Le RServlet si occupano di effettuare operazioni di lettura e restituzione di una pagina jsp al client.
 * <br>
 * Non accettano parametri in ingresso. Per questo motivo il client dovrebbe richiamarle sempre mediante uno di due metodi:
 * <ul>
 * <li>window.location.href = [servlet]</li>
 * <li>window.location.replace([servlet])</li>
 * </ul>
 * a seconda di come il client desidera gestire la history del browser.
 * <br>
 * <br>
 * @author Andrea La Rosa
 */
public class LogoutServlet extends HttpServlet {

	private static final long serialVersionUID = -8574616948128217904L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		req.getSession().invalidate();
		resp.sendRedirect(Servlets.HOME_SERVLET);
	}
}
