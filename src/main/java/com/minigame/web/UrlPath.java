package com.minigame.web;

import com.minigame.util.StringHelper;
import com.sun.net.httpserver.HttpExchange;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  UrlPath to represent a parameterized URL.
 *  Variables within the URL path are denoted by curly braces '{}' (e.g. '{userid}')
 */
public class UrlPath {

    private static class PathVar {

        private final String name;

        private final int index;

        public PathVar(String name, int index) {
            this.name = name;
            this.index = index;
        }
    }

    private static final Pattern VAR_VALUE_PATTERN = Pattern.compile("[a-zA-Z0-9]+");

    private static final Pattern VAR_NAMING_PATTERN = Pattern.compile("\\{[a-zA-Z0-9]+\\}");

    private static final Pattern URL_PARAM_REGEX = Pattern.compile("(\\?.*)?");

    private final String rawPath;

    private final String regexPath;

    private final Pattern regexPathPattern;

    private final List<PathVar> vars = new LinkedList<>();

    public UrlPath(String path) {
        this.rawPath = path;

        String[] parts = path.split("/");
        for (int i = 0; i < parts.length; i++) {
            String t = parts[i];
            Matcher m = VAR_NAMING_PATTERN.matcher(t);

            if (m.find()) {
                String expression = m.group();
                String varName = expression.substring(1, expression.length() - 1);
                vars.add(new PathVar(varName, i));
            }
        }

        this.regexPath = VAR_NAMING_PATTERN.matcher(path).replaceAll(VAR_VALUE_PATTERN.pattern());
        this.regexPathPattern = Pattern.compile(regexPath + URL_PARAM_REGEX.pattern());
    }

    public int getSizeOfPathVar() {
        return vars.size();
    }

    public String getRegexPath() {
        return regexPath;
    }

    public boolean isMatch(HttpExchange exchange) {
        String lookupPath = exchange.getRequestURI().getPath();
        return regexPathPattern.matcher(lookupPath).matches();
    }

    public Map<String, String> parsePathVarAndParameter(HttpExchange exchange) {
        Map<String, String> output = new HashMap<>(5);
        parsePathVars(exchange, output);
        parseUrlParameters(exchange, output);
        return output;
    }

    private void parsePathVars(HttpExchange exchange, Map<String, String> output) {
        String lookupPath = exchange.getRequestURI().getPath();
        String[] tokens = lookupPath.split("/");
        for (PathVar var : vars) {
            String value = tokens[var.index];
            output.put(var.name, value);
        }
    }

    private void parseUrlParameters(HttpExchange exchange, Map<String, String> output) {
        String query = exchange.getRequestURI().getQuery();
        if (StringHelper.isNullOrEmpty(query)) {
            return;
        }

        String[] parts = query.split("\\?");
        if (parts.length != 1) {
            return;
        }

        for (String param : parts[0].split("&")) {
            String[] pair = param.split("=");
            if (pair.length == 2) {
                output.put(pair[0], pair[1]);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UrlPath urlPath = (UrlPath) o;
        return regexPath.equals(urlPath.regexPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(regexPath);
    }
}
