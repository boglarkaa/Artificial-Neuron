import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

public class Gui {
    private JFrame frame;
    private JPanel xPanel, wPanel, inputPanel, globalInputPanel, activationPanel, mainPanel, controlPanel, parameterPanel, componentPanel;
    private SpinnerModel xSpinnerModel, wSpinnerModel;
    private ArrayList<JSpinner> xSpinners, wSpinners;
    private JScrollPane xScrollPane, wScrollPane;
    private JButton removeConnectionButton, addNewConnectionButton;
    private JLabel inputResultLabel, activationResultLabel;
    private DefaultListModel<String> controls, activationFormulas;
    private JList<String> controlList, formulaList;
    private JCheckBox isBinary;
    private JTextField thetaField, gField, aField;
    private JLabel thetaLabel, gLabel, aLabel;
    private Font font = new Font("SimSun", Font.BOLD, 15);
    private Color backgroundColor = new Color(243, 233, 220);
    private Color buttonColor= new Color(192, 133, 82);
    private Color foregroundColor = new Color(94, 48, 35);
    private Color selectionColor = new Color(229, 179, 135);

    public Gui() {
        // Create frame
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(750, 350);

        // Initialize panels
        inputPanel = new JPanel(new BorderLayout());
        globalInputPanel = new JPanel(new BorderLayout());
        activationPanel = new JPanel(new BorderLayout());
        mainPanel = new JPanel(new GridLayout(1, 3));
        controlPanel = new JPanel();
        xPanel = new JPanel(new GridLayout(0, 1));
        wPanel = new JPanel(new GridLayout(0, 1));

        xSpinners = new ArrayList<>();
        wSpinners = new ArrayList<>();

        //make it scrollable
        xScrollPane = new JScrollPane(xPanel);
        wScrollPane = new JScrollPane(wPanel);
        xPanel.setLayout(new BoxLayout(xPanel, BoxLayout.Y_AXIS));
        wPanel.setLayout(new BoxLayout(xPanel, BoxLayout.Y_AXIS));
        xScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        wScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        xScrollPane.setPreferredSize(new Dimension(110, 200));
        wScrollPane.setPreferredSize(new Dimension(110, 200));

        inputPanel.add(xScrollPane);
        inputPanel.add(wScrollPane);

        // Create and add first input
        addNewConnection();

        // Create control buttons for adding and removing connections
        addNewConnectionButton = new JButton("Add");
        addNewConnectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewConnection();
            }
        });

        removeConnectionButton = new JButton("Remove");
        removeConnectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeConnection();
            }
        });

        // List of global input calc methods
        controls = new DefaultListModel<>();
        controls.addElement("Sum");
        controls.addElement("Product");
        controls.addElement("Minimum");
        controls.addElement("Maximum");

        controlList = new JList<>(controls);
        controlList.setPreferredSize(new Dimension(150, 100));

        // at every selection change, recalculate globalInput
        controlList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                calculateAndShowGlobalInput();
            }
        });

        inputResultLabel = new JLabel();
        inputResultLabel.setHorizontalAlignment(JLabel.CENTER);

        // List of activation methods
        activationFormulas = new DefaultListModel<>();
        activationFormulas.addElement("Step");
        activationFormulas.addElement("Sigmoid");
        activationFormulas.addElement("Tanh");
        activationFormulas.addElement("Sign");
        activationFormulas.addElement("Linear");

        formulaList = new JList<>(activationFormulas);
        formulaList.setPreferredSize(new Dimension(150, 100));

        // at every selection change, recalculate activation
        formulaList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                calculateAndShowActivation();
            }
        });

        activationResultLabel = new JLabel();
        activationResultLabel.setHorizontalAlignment(JLabel.CENTER);

        // Checkbox for binary behavior
        isBinary = new JCheckBox("Binary");
        isBinary.setHorizontalAlignment(JCheckBox.CENTER);
        isBinary.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                calculateAndShowActivation();
            }
        });

        // labels anf fields for parameters
        thetaLabel = new JLabel("Theta:");
        thetaField = new JTextField("0", 3);
        gLabel = new JLabel("g:");
        gField = new JTextField("1", 3);
        aLabel = new JLabel("a:");
        aField = new JTextField("1", 3);

        //recalculate activation if the values in the fields change
        addDocumentListenerForField(thetaField);
        addDocumentListenerForField(aField);
        addDocumentListenerForField(gField);

        // Set up the 3 main panels
        setPanelDesign(inputPanel, "Input", xScrollPane, wScrollPane, addNewConnectionButton, removeConnectionButton);
        setPanelDesign(globalInputPanel, "Global Input Calculator");
        setPanelDesign(activationPanel, "Activation Calculator", formulaList, activationResultLabel, isBinary, thetaLabel, thetaField, gLabel, gField, aLabel, aField, activationResultLabel);

        globalInputPanel.add(controlList, BorderLayout.NORTH);
        globalInputPanel.add(inputResultLabel, BorderLayout.SOUTH);

        controlPanel.setLayout(new GridLayout(0,2 ));
        controlPanel.add(formulaList);
        controlPanel.add(isBinary);

        parameterPanel = new JPanel( new FlowLayout());
        parameterPanel.add(thetaLabel);
        parameterPanel.add(thetaField);
        parameterPanel.add(gLabel);
        parameterPanel.add(gField);
        parameterPanel.add(aLabel);
        parameterPanel.add(aField);

        inputResultLabel.setBorder(new EmptyBorder(0, 0, 50, 0));
        activationResultLabel.setBorder(new EmptyBorder(0, 0, 50, 0));

        activationPanel.add(controlPanel, BorderLayout.NORTH);
        activationPanel.add(parameterPanel, BorderLayout.CENTER);
        activationPanel.add(activationResultLabel, BorderLayout.SOUTH);

        design();

        mainPanel.add(inputPanel);
        mainPanel.add(globalInputPanel);
        mainPanel.add(activationPanel);
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private void setPanelDesign(JPanel panel, String title, JComponent... components) {
        panel.setLayout(new BorderLayout());
        Border border = BorderFactory.createTitledBorder(null, title, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, font);
        panel.setBorder(border);

        componentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        for (JComponent component : components) {
            componentPanel.add(component);
        }
        componentPanel.setBackground(backgroundColor);
        panel.add(componentPanel, BorderLayout.CENTER);
    }

    private void addNewConnection() {
        xSpinnerModel = new SpinnerNumberModel(0, -100, 100, 0.01);
        wSpinnerModel = new SpinnerNumberModel(0, -100, 100, 0.01);

        JSpinner newXSpinner = new JSpinner(xSpinnerModel);
        JSpinner newWSpinner = new JSpinner(wSpinnerModel);

        //recalculate if spinner's value changed
        newXSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (controlList.getSelectedValue() != null) {
                    calculateAndShowGlobalInput();
                }
                if(formulaList.getSelectedValue() != null){
                    calculateAndShowActivation();
                }
            }
        });

        newWSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (controlList.getSelectedValue() != null) {
                    calculateAndShowGlobalInput();
                }
                if(formulaList.getSelectedValue() != null){
                    calculateAndShowActivation();
                }
            }
        });

        newXSpinner.setPreferredSize(new Dimension(60, 20));
        newWSpinner.setPreferredSize(new Dimension(60, 20));

        newXSpinner.setFont(font);
        newWSpinner.setFont(font);

        xPanel.setLayout(new BoxLayout(xPanel, BoxLayout.Y_AXIS));
        wPanel.setLayout(new BoxLayout(wPanel, BoxLayout.Y_AXIS));

        JPanel xSpinnerPanel = new JPanel();
        JLabel xLabel = new JLabel("x:");
        xLabel.setFont(font);
        xSpinnerPanel.add(xLabel);
        xSpinnerPanel.add(newXSpinner);
        xPanel.add(xSpinnerPanel);

        JPanel wSpinnerPanel = new JPanel();
        JLabel wLabel = new JLabel("w:");
        wLabel.setFont(font);
        wSpinnerPanel.add(wLabel);
        wSpinnerPanel.add(newWSpinner);
        wPanel.add(wSpinnerPanel);

        xSpinners.add(newXSpinner);
        wSpinners.add(newWSpinner);

        xSpinnerPanel.setBackground(backgroundColor);
        wSpinnerPanel.setBackground(backgroundColor);

        refreshPanels();
    }

    private void removeConnection() {
        //delete spinner from UI and from arraylist
        if (xSpinners.size() > 1) {
            xPanel.remove(xSpinners.size() - 1);
            wPanel.remove(wSpinners.size() - 1);
            xSpinners.remove(xSpinners.size() - 1);
            wSpinners.remove(wSpinners.size() - 1);
            refreshPanels();
        } else {
            JOptionPane.showMessageDialog(frame, "You must have at least one input!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshPanels() {
        xPanel.revalidate();
        xPanel.repaint();
        wPanel.revalidate();
        wPanel.repaint();
    }

    private void calculateAndShowGlobalInput() {
        if (controlList.getSelectedValue().equals("Sum")) {
            inputResultLabel.setText(String.format("%.8f", GlobalInputCalc.sum(getInput())));
        } else if (controlList.getSelectedValue().equals("Product")) {
            inputResultLabel.setText(String.format("%.8f", GlobalInputCalc.prod(getInput())));
        } else if (controlList.getSelectedValue().equals("Minimum")) {
            inputResultLabel.setText(String.format("%.8f", GlobalInputCalc.minimum(getInput())));
        } else if (controlList.getSelectedValue().equals("Maximum")) {
            inputResultLabel.setText(String.format("%.8f", GlobalInputCalc.maximum(getInput())));
        }
    }

    private void calculateAndShowActivation() {
        ActivationCalc activation = new ActivationCalc(Double.parseDouble(inputResultLabel.getText()));
        if (formulaList.getSelectedValue().equals("Step")) {
            activationResultLabel.setText(Double.toString(activation.step(Double.parseDouble(thetaField.getText()))));
        } else if (formulaList.getSelectedValue().equals("Sign")) {
            activationResultLabel.setText(Double.toString(activation.sign(Double.parseDouble(thetaField.getText()))));
        } else if (formulaList.getSelectedValue().equals("Sigmoid")) {
            if (isBinary.isSelected()) {
                if (activation.sigmoid(0, 1) >= 0.5) {
                    activationResultLabel.setText("1.0");
                } else {
                    activationResultLabel.setText("0.0");
                }
            } else {
                activationResultLabel.setText(String.format("%.8f", activation.sigmoid(Double.parseDouble(thetaField.getText()), Double.parseDouble(gField.getText()))));
            }
        } else if (formulaList.getSelectedValue().equals("Tanh")) {
            if (isBinary.isSelected()) {
                if (activation.tanh(Double.parseDouble(thetaField.getText()), Double.parseDouble(gField.getText())) >= 0) {
                    activationResultLabel.setText("1.0");
                } else {
                    activationResultLabel.setText("-1.0");
                }
            } else {
                activationResultLabel.setText(String.format("%.8f", activation.tanh(Double.parseDouble(thetaField.getText()), Double.parseDouble(gField.getText()))));
            }
        } else if (formulaList.getSelectedValue().equals("Linear")) {
            if (isBinary.isSelected()) {
                if (activation.sigmoid(Double.parseDouble(thetaField.getText()), Double.parseDouble(gField.getText())) >= 0) {
                    activationResultLabel.setText("1.0");
                } else {
                    activationResultLabel.setText("-1.0");
                }
            } else {
                activationResultLabel.setText(String.format("%.8f", activation.linear(Double.parseDouble(thetaField.getText()), Double.parseDouble(aField.getText()))));
            }
        }
        frame.revalidate();
        frame.repaint();
    }

    private Neuron getInput() {
        ArrayList<Double> x = new ArrayList<>();
        ArrayList<Double> w = new ArrayList<>();
        for (int i = 0; i < xSpinners.size(); i++) {
            x.add((Double) xSpinners.get(i).getValue());
            w.add((Double) wSpinners.get(i).getValue());
        }
        return new Neuron(x, w);
    }

    private void addDocumentListenerForField(JTextField field) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                calculateAndShowActivation();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                System.out.println(field.getName() + " must have a value!");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                calculateAndShowActivation();
            }
        });
    }

    public void design() {
        setBackgroundColor(backgroundColor, inputPanel, globalInputPanel, activationPanel, formulaList, controlList,
                controlPanel, parameterPanel, isBinary, xPanel, wPanel,  inputResultLabel);
        setFont(font, thetaField, thetaLabel, aField, aLabel, gField, gLabel, isBinary, controlList, formulaList,
                addNewConnectionButton, removeConnectionButton, inputResultLabel, activationResultLabel);
        setBackgroundColor(buttonColor, addNewConnectionButton, removeConnectionButton);
        setForegroundColor(backgroundColor, addNewConnectionButton, removeConnectionButton);
        setForegroundColor(foregroundColor, thetaField, thetaLabel, aField, aLabel, gField, gLabel, isBinary,
                controlList, formulaList, inputResultLabel, activationResultLabel);
        formulaList.setSelectionBackground(selectionColor);
        controlList.setSelectionBackground(selectionColor);
    }

    private void setBackgroundColor(Color color, JComponent... components) {
        for (JComponent component : components) {
            component.setBackground(color);
        }
    }
    private void setFont(Font font, JComponent... components) {
        for (JComponent component : components) {
            component.setFont(font);
        }
    }

    private void setForegroundColor(Color color, JComponent... components) {
        for (JComponent component : components) {
            component.setForeground(color);
        }
    }
}
