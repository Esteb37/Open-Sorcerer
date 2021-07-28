package com.example.opensorcerer.models;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;

import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.opensorcerer.R;
import com.google.android.material.chip.ChipDrawable;
import com.parse.ParseFile;

import org.apache.commons.lang3.text.WordUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Abstract class for general use static methods
 */
@SuppressWarnings("SpellCheckingInspection")
public abstract class Tools {

    /**
     * List of all recognized programming languages
     */
    private static final String[] languages = {"(Visual) FoxPro: FoxPro, " +
            "Fox Pro, VFP", "1C:Enterprise script", "4th Dimension/4D: 4D, 4th " +
            "Dimension", "ABAP", "ABC: ABC (exceptions: -tv -channel)", "ActionScript: ActionScript, AS1, AS2, AS3",
            "Ada", "Agilent VEE", "Algol", "Alice: Alice (confidence: 90%)", "Angelscript", "Apex", "APL", "Applescript",
            "Arc", "AspectJ", "Assembly language: Assembly, Assembly language", "ATLAS",
            "AutoHotkey: AutoHotkey, AHK", "AutoIt", "AutoLISP", "Automator", "Avenue",
            "Awk: Awk, Mawk, Gawk, Nawk", "B4X", "Ballerina", "Bash", "Basic: Basic (confidence: 0%)",
            "BBC BASIC", "bc", "BCPL", "BETA: BETA (confidence: 10%)", "BlitzMax: BlitzMax, BlitzBasic, Blitz Basic",
            "Boo", "Bourne shell: Bourne shell, sh", "Brainfuck", "C shell: Csh, C shell (confidence: 90%)",
            "C#: C#, C-Sharp, C Sharp, CSharp, CSharp.NET, C#.NET", "C++", "C++/CLI", "C-Omega",
            "C: C (exceptions: -'Objective-C')", "Caml", "Ceylon", "CFML: CFML, ColdFusion",
            "cg: cg (confidence: 80%, exceptions: -'computer game' -'computer graphics')",
            "Ch: Ch (exceptions: +ChScite)", "Chapel: Chapel (exceptions: -christ)",
            "CHILL", "CIL", "CL (OS/400): CL (exceptions: -Lisp), CLLE", "Clarion",
            "Classic Visual Basic: Visual Basic (confidence: 50%), VB (confidence: 50%), VBA, VB6",
            "Clean: Clean (confidence: 43%)", "Clipper", "CLIPS", "Clojure: Clojure, ClojureScript", "CLU", "COBOL", "Cobra", "CoffeeScript", "COMAL", "Common Lisp", "Crystal: Crystal (confidence: 61%, exceptions: -healing), crystallang", "cT", "Curl", "D: D (confidence: 90%, exceptions: -'3-D programming' -'DTrace'), dlang", "Dart", "DCL", "Delphi/Object Pascal: DwScript, Object Pascal, Delphi, Delphi.NET, Pascal (confidence: 95%)", "DiBOL: DBL, Synergy/DE, DIBOL", "Dylan", "E: E (exceptions: +specman)", "ECMAScript", "EGL", "Eiffel", "Elixir", "Elm", "Emacs Lisp: Emacs Lisp, Elips", "Emerald", "Erlang", "Etoys", "Euphoria", "EXEC", "F#: F#, F-Sharp, FSharp, F Sharp", "Factor", "Falcon", "Fantom", "Felix: Felix (confidence: 86%)", "Forth", "Fortran", "Fortress", "FreeBASIC", "Gambas", "GAMS", "GLSL", "GML: GML, GameMaker Language", "GNU Octave", "Go: Go, Golang", "Gosu", "Groovy: Groovy, GPATH, GSQL, Groovy++", "Hack", "Harbour", "Haskell", "Haxe", "Heron", "HPL", "HyperTalk", "Icon: Icon (confidence: 90%)", "IDL: IDL (exceptions: -corba -interface)", "Idris", "Inform", "Informix-4GL", "INTERCAL", "Io", "Ioke", "J#", "J: J (confidence: 50%)", "JADE", "Java", "JavaFX Script", "JavaScript: JavaScript, JS, SSJS", "JScript", "JScript.NET", "Julia: Julia, Julialang, julia-lang", "Korn shell: Korn shell, ksh", "Kotlin", "LabVIEW", "Ladder Logic", "Lasso", "Limbo", "Lingo", "Lisp", "LiveCode: Revolution, LiveCode", "Logo: Logo (confidence: 90%, exceptions: -tv)", "LotusScript", "LPC", "Lua: Lua, LuaJIT", "Lustre", "M4", "MAD: MAD (confidence: 50%)", "Magic: Magic (confidence: 50%)", "Magik", "Malbolge", "MANTIS", "Maple", "MATLAB", "Max/MSP", "MAXScript", "MDX", "MEL", "Mercury", "Miva", "ML", "Modula-2", "Modula-3", "Monkey", "MOO", "Moto", "MQL4: MQL4, MQL5", "MS-DOS batch", "MUMPS", "NATURAL", "Nemerle", "NetLogo", "Nim: Nim, Nimrod", "NQC", "NSIS", "NXT-G", "Oberon", "Object Rexx", "Objective-C: Objective-C, objc, obj-c", "OCaml: Objective Caml, OCaml", "Occam", "OpenCL", "OpenEdge ABL: Progress, Progress 4GL, ABL, Advanced Business Language, OpenEdge", "OPL", "Oxygene", "Oz", "Paradox", "Pascal: Pascal (confidence: 5%)", "Perl", "PHP", "Pike", "PILOT: PILOT (confidence: 50%, exceptions: -'Palm Pilot programming')", "PL/I: PL/1, PL/I", "PL/SQL", "Pliant", "Pony", "PostScript: PostScript, PS", "POV-Ray", "PowerBasic", "PowerScript", "PowerShell", "Processing: Processing (exceptions: +'sketchbook')", "Programming Without Coding Technology: Programming Without Coding Technology, PWCT", "Prolog", "Pure Data: Pure Data, PD", "PureBasic", "Python", "Q", "R: R (confidence: 90%, exceptions: +'statistical')", "Racket", "Raku: Perl 6, Raku", "REBOL", "Red: Red (confidence: 20%)", "REXX", "Ring", "RPG: RPG (confidence: 80%, exceptions: -role), RPGLE, ILERPG, RPGIV, RPGIII, RPG400, RPGII, RPG4", "Ruby", "Rust: Rust, Rustlang", "S-PLUS: S-PLUS (exceptions: +statistical)", "S: S (exceptions: +statistical)", "SAS", "Sather", "Scala", "Scheme: Scheme (exceptions: -tv -channel)", "Scratch", "sed", "Seed7", "SIGNAL: SIGNAL (confidence: 10%)", "Simula", "Simulink", "Slate: Slate (confidence: 57%)", "Small Basic", "Smalltalk", "Smarty", "Snap!", "SNOBOL", "Solidity", "SPARK", "SPSS", "SQL", "SQR", "Squeak", "Squirrel", "Standard ML: Standard ML, SML", "Stata", "Structured Text", "Suneido", "SuperCollider: SuperCollider (confidence: 80%)", "Swift", "TACL", "Tcl: Tcl/Tk, Tcl", "tcsh", "Tex", "thinBasic", "TOM: TOM (confidence: 50%)", "Transact-SQL: T-SQL, Transact-SQL, TSQL", "TypeScript: TypeScript, TS", "Uniface", "Vala/Genie: Vala, Genie", "VBScript", "Verilog", "VHDL", "Visual Basic: Visual Basic .NET, VB.NET, Visual Basic.NET, Visual Basic (confidence: 50%), VB (confidence: 50%)", "WebAssembly", "WebDNA", "Whitespace", "Wolfram", "X10", "xBase", "XBase++", "XC", "Xen", "Xojo: REALbasic, Xojo", "XPL", "XQuery", "XSLT", "Xtend", "yacc", "Yorick", "Z shell: Z shell, zsh", "Zig: Zig, zlang"};

    /**
     * Programming languages list getter
     */
    public static String[] getLanguages() {
        return languages;
    }

    /**
     * Turns a list of tags or languages into a formatted and cleaned comma
     * separated text
     */
    public static String listToString(List<String> list) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {

            //Trim trailing spaces
            String item = list.get(i).trim();

            //Only include non empty tags
            if (!item.equals("")) {
                str.append(item).append(", ");
            }
        }

        //Remove the trailing comma
        return str.length() > 1 ? str.substring(0,str.length() - 2) : str.toString();
    }

    /**
     * Turns a bitmap image into a parse file
     */
    public static ParseFile bitmapToParseFile(Bitmap imageBitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        return new ParseFile("image.png", imageByte);
    }

    /**
     * Creates an intent for selecting a file source and then selecting an image
     */
    public static Intent createChooserIntent() {

        //Prompt the user to pick a file from their device
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        //Prompt the user to pick the folder with the desired image
        Intent pickIntent = new Intent(Intent.ACTION_PICK);
        pickIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

        //Prompt the user to select the image
        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        return chooserIntent;
    }

    /**
     * Creates a new chip token from the last inputed item in an editable
     */
    public static Editable addChip(Context context, Editable editable, int spannedLength) {

        ChipDrawable chip = ChipDrawable.createFromResource(context, R.xml.chip);
        chip.setText(editable.subSequence(spannedLength, editable.length()).toString().replace(",", ""));
        chip.setBounds(0, 0, chip.getIntrinsicWidth(), chip.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(chip);
        editable.setSpan(span, spannedLength, editable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return editable;
    }

    /**
     * Creates a new chip token from the last inputed item in an editable
     */
    public static void addChip(Context context, String item, AppCompatMultiAutoCompleteTextView chipInput) {

        Editable editable = chipInput.getText();
        String inputString = editable.toString();
        int spannedLength = inputString.length();
        chipInput.setText(String.format("%s%s", inputString, item));
        editable = chipInput.getText();
        ChipDrawable chip = ChipDrawable.createFromResource(context, R.xml.chip);
        chip.setText(item);
        chip.setBounds(0, 0, chip.getIntrinsicWidth(), chip.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(chip);

        editable.setSpan(span, spannedLength, spannedLength + item.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /**
     * Replaces the specified layout with the specified fragment
     */
    public static void loadFragment(Context context, Fragment fragment, int containerId) {
        final FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(containerId, fragment, fragment.getClass().getName());
        transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();
    }

    /**
     * Replace the specified layout with the specified fragment with animation
     */
    public static void navigateToFragment(Context context, Fragment fragment, int containerId, String direction) {
        final FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if(direction.equals("right_to_left")) {
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (direction.equals("left_to_right")) {
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
    }

        transaction.replace(containerId, fragment, fragment.getClass().getName());
        transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();
    }

    /**
     * Fetches an image from a URL source and turns it to bitmap
     */
    public static Bitmap getBitmapFromURL(String src) {
        try {
            //Setup the request for the image
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            //Get the image as an input stream
            InputStream input = connection.getInputStream();
            //Get the bipmap from the input stream
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ParseFile getParseImageFromUrl(String url){
        return bitmapToParseFile(Objects.requireNonNull(getBitmapFromURL(url)));
    }

    /**
     * Gets the local link for a repo from the URL
     */
    public static String getRepositoryName(String repoLink) {
        return repoLink.split("github.com/")[1];
    }

    /**
     * Formats a repository title
     */
    @SuppressWarnings("deprecation")
    public static String formatTitle(String title) {
        return WordUtils.capitalizeFully(title.replace("-"," "));
    }

    /**
     * Creates a relative timestamp from a date object
     */
    public static String getRelativeTimeStamp(Date createdAt) {
        int SECOND_MILLIS = 1000;
        int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        int DAY_MILLIS = 24 * HOUR_MILLIS;

        try {
            createdAt.getTime();
            long time = createdAt.getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " m";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " h";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + " d";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
