package analizadorLexico;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.io.File;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Menu extends JFrame {
	
	private ArrayList<Character> Frase = new ArrayList<Character>();
	JFileChooser seleccionar = new JFileChooser();
	File archivo;
	FileInputStream entrada;
	FileOutputStream salida;
	private JPanel contentPane;

	//Método auxiliar para poder abrir archivos txt
	private String AbrirArchivo(File archivo) {
		String documento="";
		try
		{
			entrada = new FileInputStream(archivo);
			int ascci;
			while((ascci=entrada.read())!=-1)
			{
				char caracter = (char)ascci;
				documento+=caracter;
			}
		}
		catch(Exception e)
		{
			
		}
		return documento;
	}
	
	//Método auxiliar para poder Guardar el archivom transforma el archivo en byte para poder ser guardado
	public String GuardarArchivo(File archivo, String documento)
	{
		String mensaje = null;
		try {
			salida = new FileOutputStream(archivo);
			byte[] bytxt = documento.getBytes();
			salida.write(bytxt);
			mensaje="Archivo Guardado";
		}catch (Exception e)
		{
			
		}
		return mensaje;
	}
	
	//Se obtiene el último elemento que no contenga una palabra
	private int findLastNonWordChar (String text, int index)
	{
		while(--index >= 0)
		{
			if(String.valueOf(text.charAt(index)).matches("\\W"))
			{
				break;
			}
		}
		return index;
	}
	
	//Se obtiene obtiene la primera no letra del programa, se utiliza la expresión \\W para ello
	// \\W = Nonword 
	private int findFirstNonWordChar (String text, int index)
	{
		while(index < text.length())
		{
			if(String.valueOf(text.charAt(index)).matches("\\W"))
			{
				break;
			}
			index ++;
		}
		return index;
	}
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu frame = new Menu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Menu() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 385);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		StyleContext context = new StyleContext();
		
		JButton btnGuardar = new JButton("GUARDAR");
		
		btnGuardar.setBounds(324, 293, 117, 25);
		contentPane.add(btnGuardar);
		
		JButton btnBorrar = new JButton("ABRIR");
		
		btnBorrar.setBounds(195, 293, 117, 25);
		contentPane.add(btnBorrar);
		
		final StyleContext cont = StyleContext.getDefaultStyleContext();
		
		//Se generan los colores de los atributos del texto
		final AttributeSet attr = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLUE);
		final AttributeSet attrGreen = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.GREEN);
		final AttributeSet attrBlack = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
		
		//Se lee la entrada del teclado, se lee la frase que se inserta y mediante expresiones regulares se determina
		//Si pertenece a una variable, a un signo de puntuación o al texto normal
		DefaultStyledDocument document = new DefaultStyledDocument() 
		{
			public void insertString(int offset, String str, AttributeSet a) throws BadLocationException
			{
				super.insertString(offset, str, a);
				
				String text = getText(0, getLength());
				int before = findLastNonWordChar(text, offset);
				if(before <0) before = 0;
				int after = findFirstNonWordChar(text, offset + str.length());
				int wordL = before;
				int wordR = before;
				
				while (wordR <= after) {
					if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W"))
					{
						if(text.substring(wordL, wordR).matches("((\\W)*((\\.)|(,)|((\\:)(\\-))*))"))
						{
							setCharacterAttributes(wordL, wordR - wordL, attr, false);	
						}
						else if(text.substring(wordL, wordR).matches("(\\W)*((_)|[A-Z])([a-zA-Z])*"))
						{
							setCharacterAttributes(wordL, wordR - wordL, attrGreen, false);	
						}
						else
							setCharacterAttributes(wordL, wordR - wordL, attrBlack, false);	
						wordL = wordR;
					}
					wordR++;
				}
			}
		};
		JTextPane textPane = new JTextPane(document);
		
		//Se genera el evento para guardar el archivo con terminación txt con un event listener
		btnGuardar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(seleccionar.showDialog(null,  "Guardar") == JFileChooser.APPROVE_OPTION)
				{
					archivo=seleccionar.getSelectedFile();
					if(archivo.getName().endsWith("txt"))
					{
						String Documento = textPane.getText();
						String mensaje = GuardarArchivo(archivo, Documento);
						if(mensaje!=null) {
							JOptionPane.showMessageDialog(null, mensaje);
						}
						else {
							JOptionPane.showMessageDialog(null, "Archivo no compatible");
						}
					}else {
						JOptionPane.showMessageDialog(null, "Guardar Documento de texto");
					}
				}
			}
		});
		
		
		//Se genera evento para guardar elemento con un event listener
		btnBorrar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(seleccionar.showDialog(null, "Abrir")==JFileChooser.APPROVE_OPTION)
				{
					archivo = seleccionar.getSelectedFile();
					if(archivo.canRead())
					{
						if(archivo.getName().endsWith("txt"))
						{
							String documento = AbrirArchivo(archivo);
							textPane.setText(documento);
						}else {
							JOptionPane.showMessageDialog(null, "Archivo no compatible");
						}
					}
				}
			}
		});
		textPane.setBounds(42, 12, 360, 232);
		contentPane.add(textPane);
	}
}
