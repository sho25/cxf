begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MultivaluedMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|PathSegment
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|utils
operator|.
name|HttpUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|utils
operator|.
name|JAXRSUtils
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|URITemplate
block|{
specifier|public
specifier|static
specifier|final
name|String
name|TEMPLATE_PARAMETERS
init|=
literal|"jaxrs.template.parameters"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|LIMITED_REGEX_SUFFIX
init|=
literal|"(/.*)?"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FINAL_MATCH_GROUP
init|=
literal|"FINAL_MATCH_GROUP"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_PATH_VARIABLE_REGEX
init|=
literal|"([^/]+?)"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CHARACTERS_TO_ESCAPE
init|=
literal|".*+"
decl_stmt|;
specifier|private
specifier|final
name|String
name|template
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|variables
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|customVariables
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Pattern
name|templateRegexPattern
decl_stmt|;
specifier|private
specifier|final
name|String
name|literals
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|UriChunk
argument_list|>
name|uriChunks
decl_stmt|;
specifier|public
name|URITemplate
parameter_list|(
name|String
name|theTemplate
parameter_list|)
block|{
name|template
operator|=
name|theTemplate
expr_stmt|;
name|StringBuilder
name|literalChars
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|StringBuilder
name|patternBuilder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|CurlyBraceTokenizer
name|tok
init|=
operator|new
name|CurlyBraceTokenizer
argument_list|(
name|template
argument_list|)
decl_stmt|;
name|uriChunks
operator|=
operator|new
name|ArrayList
argument_list|<
name|UriChunk
argument_list|>
argument_list|()
expr_stmt|;
while|while
condition|(
name|tok
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|templatePart
init|=
name|tok
operator|.
name|next
argument_list|()
decl_stmt|;
name|UriChunk
name|chunk
init|=
name|UriChunk
operator|.
name|createUriChunk
argument_list|(
name|templatePart
argument_list|)
decl_stmt|;
name|uriChunks
operator|.
name|add
argument_list|(
name|chunk
argument_list|)
expr_stmt|;
if|if
condition|(
name|chunk
operator|instanceof
name|Literal
condition|)
block|{
name|String
name|encodedValue
init|=
name|HttpUtils
operator|.
name|encodePartiallyEncoded
argument_list|(
name|chunk
operator|.
name|getValue
argument_list|()
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|String
name|substr
init|=
name|escapeCharacters
argument_list|(
name|encodedValue
argument_list|)
decl_stmt|;
name|literalChars
operator|.
name|append
argument_list|(
name|substr
argument_list|)
expr_stmt|;
name|patternBuilder
operator|.
name|append
argument_list|(
name|substr
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|chunk
operator|instanceof
name|Variable
condition|)
block|{
name|Variable
name|var
init|=
operator|(
name|Variable
operator|)
name|chunk
decl_stmt|;
name|variables
operator|.
name|add
argument_list|(
name|var
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|var
operator|.
name|getPattern
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|customVariables
operator|.
name|add
argument_list|(
name|var
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|patternBuilder
operator|.
name|append
argument_list|(
literal|'('
argument_list|)
expr_stmt|;
name|patternBuilder
operator|.
name|append
argument_list|(
name|var
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|patternBuilder
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|patternBuilder
operator|.
name|append
argument_list|(
name|DEFAULT_PATH_VARIABLE_REGEX
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|literals
operator|=
name|literalChars
operator|.
name|toString
argument_list|()
expr_stmt|;
name|int
name|endPos
init|=
name|patternBuilder
operator|.
name|length
argument_list|()
operator|-
literal|1
decl_stmt|;
name|boolean
name|endsWithSlash
init|=
operator|(
name|endPos
operator|>=
literal|0
operator|)
condition|?
name|patternBuilder
operator|.
name|charAt
argument_list|(
name|endPos
argument_list|)
operator|==
literal|'/'
else|:
literal|false
decl_stmt|;
if|if
condition|(
name|endsWithSlash
condition|)
block|{
name|patternBuilder
operator|.
name|deleteCharAt
argument_list|(
name|endPos
argument_list|)
expr_stmt|;
block|}
name|patternBuilder
operator|.
name|append
argument_list|(
name|LIMITED_REGEX_SUFFIX
argument_list|)
expr_stmt|;
name|templateRegexPattern
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|patternBuilder
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getLiteralChars
parameter_list|()
block|{
return|return
name|literals
return|;
block|}
specifier|public
name|String
name|getValue
parameter_list|()
block|{
return|return
name|template
return|;
block|}
comment|/**      * List of all variables in order of appearance in template.      *       * @return unmodifiable list of variable names w/o patterns, e.g. for "/foo/{v1:\\d}/{v2}" returned list      *         is ["v1","v2"].      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getVariables
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|variables
argument_list|)
return|;
block|}
comment|/**      * List of variables with patterns (regexps). List is subset of elements from {@link #getVariables()}.      *       * @return unmodifiable list of variables names w/o patterns.      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getCustomVariables
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|customVariables
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|String
name|escapeCharacters
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|expression
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|char
name|ch
init|=
name|expression
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|isReservedCharacter
argument_list|(
name|ch
argument_list|)
condition|?
literal|"\\"
operator|+
name|ch
else|:
name|ch
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|boolean
name|isReservedCharacter
parameter_list|(
name|char
name|ch
parameter_list|)
block|{
return|return
name|CHARACTERS_TO_ESCAPE
operator|.
name|indexOf
argument_list|(
name|ch
argument_list|)
operator|!=
operator|-
literal|1
return|;
block|}
specifier|public
name|boolean
name|match
parameter_list|(
name|String
name|uri
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|templateVariableToValue
parameter_list|)
block|{
if|if
condition|(
name|uri
operator|==
literal|null
condition|)
block|{
return|return
operator|(
name|templateRegexPattern
operator|==
literal|null
operator|)
condition|?
literal|true
else|:
literal|false
return|;
block|}
if|if
condition|(
name|templateRegexPattern
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Matcher
name|m
init|=
name|templateRegexPattern
operator|.
name|matcher
argument_list|(
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
if|if
condition|(
name|uri
operator|.
name|contains
argument_list|(
literal|";"
argument_list|)
condition|)
block|{
comment|// we might be trying to match one or few path segments
comment|// containing matrix
comment|// parameters against a clear path segment as in @Path("base").
name|List
argument_list|<
name|PathSegment
argument_list|>
name|pList
init|=
name|JAXRSUtils
operator|.
name|getPathSegments
argument_list|(
name|template
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|PathSegment
argument_list|>
name|uList
init|=
name|JAXRSUtils
operator|.
name|getPathSegments
argument_list|(
name|uri
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|uList
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
expr_stmt|;
if|if
condition|(
name|pList
operator|.
name|size
argument_list|()
operator|>
name|i
operator|&&
name|pList
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getPath
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|'{'
argument_list|)
operator|==
operator|-
literal|1
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|uList
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
name|HttpUtils
operator|.
name|fromPathSegment
argument_list|(
name|uList
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|uri
operator|=
name|sb
operator|.
name|toString
argument_list|()
expr_stmt|;
name|m
operator|=
name|templateRegexPattern
operator|.
name|matcher
argument_list|(
name|uri
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
comment|// Assign the matched template values to template variables
name|int
name|groupCount
init|=
name|m
operator|.
name|groupCount
argument_list|()
decl_stmt|;
name|int
name|i
init|=
literal|1
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|variables
control|)
block|{
while|while
condition|(
name|i
operator|<=
name|groupCount
condition|)
block|{
name|String
name|value
init|=
name|m
operator|.
name|group
argument_list|(
name|i
operator|++
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
operator|||
name|value
operator|.
name|length
argument_list|()
operator|==
literal|0
operator|&&
name|i
operator|<
name|groupCount
condition|)
block|{
continue|continue;
block|}
name|templateVariableToValue
operator|.
name|add
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
comment|// The right hand side value, might be used to further resolve
comment|// sub-resources.
name|String
name|finalGroup
init|=
name|i
operator|>
name|groupCount
condition|?
literal|"/"
else|:
name|m
operator|.
name|group
argument_list|(
name|groupCount
argument_list|)
decl_stmt|;
name|templateVariableToValue
operator|.
name|putSingle
argument_list|(
name|FINAL_MATCH_GROUP
argument_list|,
name|finalGroup
operator|==
literal|null
condition|?
literal|"/"
else|:
name|finalGroup
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|/**      * Substitutes template variables with listed values. List of values is counterpart for      * {@link #getVariables() list of variables}. When list of value is shorter than variables substitution      * is partial. When variable has pattern, value must fit to pattern, otherwise      * {@link IllegalArgumentException} is thrown.      *<p>      * Example1: for template "/{a}/{b}/{a}" {@link #getVariables()} returns "[a, b, a]"; providing here list      * of value "[foo, bar, baz]" results with "/foo/bar/baz".      *<p>      * Example2: for template "/{a}/{b}/{a}" providing list of values "[foo]" results with "/foo/{b}/{a}".      *       * @param values values for variables      * @return template with bound variables.      * @throws IllegalArgumentException when values is null, any value does not match pattern etc.      */
specifier|public
name|String
name|substitute
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|values
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
if|if
condition|(
name|values
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"values is null"
argument_list|)
throw|;
block|}
name|Iterator
argument_list|<
name|String
argument_list|>
name|iter
init|=
name|values
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|UriChunk
name|chunk
range|:
name|uriChunks
control|)
block|{
if|if
condition|(
name|chunk
operator|instanceof
name|Variable
condition|)
block|{
name|Variable
name|var
init|=
operator|(
name|Variable
operator|)
name|chunk
decl_stmt|;
if|if
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|value
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|var
operator|.
name|matches
argument_list|(
name|value
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Value '"
operator|+
name|value
operator|+
literal|"' does not match variable "
operator|+
name|var
operator|.
name|getName
argument_list|()
operator|+
literal|" with pattern "
operator|+
name|var
operator|.
name|getPattern
argument_list|()
argument_list|)
throw|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
name|var
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
name|chunk
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Substitutes template variables with mapped values. Variables are mapped to values; if not all variables      * are bound result will still contain variables. Note that all variables with the same name are replaced      * by one value.      *<p>      * Example: for template "/{a}/{b}/{a}" {@link #getVariables()} returns "[a, b, a]"; providing here      * mapping "[a: foo, b: bar]" results with "/foo/bar/foo" (full substitution) and for mapping "[b: baz]"      * result is "{a}/baz/{a}" (partial substitution).      *       * @param valuesMap map variables to their values; on each value Object.toString() is called.      * @return template with bound variables.      */
specifier|public
name|String
name|substitute
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|?
extends|extends
name|Object
argument_list|>
name|valuesMap
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
if|if
condition|(
name|valuesMap
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"valuesMap is null"
argument_list|)
throw|;
block|}
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|UriChunk
name|chunk
range|:
name|uriChunks
control|)
block|{
if|if
condition|(
name|chunk
operator|instanceof
name|Variable
condition|)
block|{
name|Variable
name|var
init|=
operator|(
name|Variable
operator|)
name|chunk
decl_stmt|;
name|Object
name|value
init|=
name|valuesMap
operator|.
name|get
argument_list|(
name|var
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|String
name|sval
init|=
name|value
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|var
operator|.
name|matches
argument_list|(
name|sval
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Value '"
operator|+
name|sval
operator|+
literal|"' does not match variable "
operator|+
name|var
operator|.
name|getName
argument_list|()
operator|+
literal|" with pattern "
operator|+
name|var
operator|.
name|getPattern
argument_list|()
argument_list|)
throw|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Template variable "
operator|+
name|var
operator|.
name|getName
argument_list|()
operator|+
literal|" has no matching value"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
name|chunk
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Encoded literal characters surrounding template variables,       * ex. "a {id} b" will be encoded to "a%20{id}%20b"       * @return encoded value      */
specifier|public
name|String
name|encodeLiteralCharacters
parameter_list|()
block|{
specifier|final
name|float
name|encodedRatio
init|=
literal|1.5f
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
call|(
name|int
call|)
argument_list|(
name|encodedRatio
operator|*
name|template
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|UriChunk
name|chunk
range|:
name|uriChunks
control|)
block|{
name|String
name|val
init|=
name|chunk
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|chunk
operator|instanceof
name|Literal
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|HttpUtils
operator|.
name|encodePartiallyEncoded
argument_list|(
name|val
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
name|val
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|URITemplate
name|createTemplate
parameter_list|(
name|Path
name|path
parameter_list|)
block|{
return|return
name|createTemplate
argument_list|(
name|path
operator|==
literal|null
condition|?
literal|null
else|:
name|path
operator|.
name|value
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|URITemplate
name|createTemplate
parameter_list|(
name|String
name|pathValue
parameter_list|)
block|{
if|if
condition|(
name|pathValue
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|URITemplate
argument_list|(
literal|"/"
argument_list|)
return|;
block|}
if|if
condition|(
operator|!
name|pathValue
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|pathValue
operator|=
literal|"/"
operator|+
name|pathValue
expr_stmt|;
block|}
return|return
operator|new
name|URITemplate
argument_list|(
name|pathValue
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|int
name|compareTemplates
parameter_list|(
name|URITemplate
name|t1
parameter_list|,
name|URITemplate
name|t2
parameter_list|)
block|{
name|String
name|l1
init|=
name|t1
operator|.
name|getLiteralChars
argument_list|()
decl_stmt|;
name|String
name|l2
init|=
name|t2
operator|.
name|getLiteralChars
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|l1
operator|.
name|equals
argument_list|(
name|l2
argument_list|)
condition|)
block|{
comment|// descending order
return|return
name|l1
operator|.
name|length
argument_list|()
operator|<
name|l2
operator|.
name|length
argument_list|()
condition|?
literal|1
else|:
operator|-
literal|1
return|;
block|}
name|int
name|g1
init|=
name|t1
operator|.
name|getVariables
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|int
name|g2
init|=
name|t2
operator|.
name|getVariables
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
comment|// descending order
name|int
name|result
init|=
name|g1
operator|<
name|g2
condition|?
literal|1
else|:
name|g1
operator|>
name|g2
condition|?
operator|-
literal|1
else|:
literal|0
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|0
condition|)
block|{
name|int
name|gCustom1
init|=
name|t1
operator|.
name|getCustomVariables
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|int
name|gCustom2
init|=
name|t2
operator|.
name|getCustomVariables
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|gCustom1
operator|!=
name|gCustom2
condition|)
block|{
comment|// descending order
return|return
name|gCustom1
operator|<
name|gCustom2
condition|?
literal|1
else|:
operator|-
literal|1
return|;
block|}
block|}
return|return
name|result
return|;
block|}
comment|/**      * Stringified part of URI. Chunk is not URI segment; chunk can span over multiple URI segments or one URI      * segments can have multiple chunks. Chunk is used to decompose URI of {@link URITemplate} into literals      * and variables. Example: "foo/bar/{baz}{blah}" is decomposed into chunks: "foo/bar", "{baz}" and      * "{blah}".      */
specifier|private
specifier|abstract
specifier|static
class|class
name|UriChunk
block|{
comment|/**          * Creates object form string.          *           * @param uriChunk stringified uri chunk          * @return If param has variable form then {@link Variable} instance is created, otherwise chunk is          *         treated as {@link Literal}.          */
specifier|public
specifier|static
name|UriChunk
name|createUriChunk
parameter_list|(
name|String
name|uriChunk
parameter_list|)
block|{
if|if
condition|(
name|uriChunk
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|uriChunk
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"uriChunk is empty"
argument_list|)
throw|;
block|}
name|UriChunk
name|uriChunkRepresentation
init|=
name|Variable
operator|.
name|create
argument_list|(
name|uriChunk
argument_list|)
decl_stmt|;
if|if
condition|(
name|uriChunkRepresentation
operator|==
literal|null
condition|)
block|{
name|uriChunkRepresentation
operator|=
name|Literal
operator|.
name|create
argument_list|(
name|uriChunk
argument_list|)
expr_stmt|;
block|}
return|return
name|uriChunkRepresentation
return|;
block|}
specifier|public
specifier|abstract
name|String
name|getValue
parameter_list|()
function_decl|;
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getValue
argument_list|()
return|;
block|}
block|}
specifier|private
specifier|static
specifier|final
class|class
name|Literal
extends|extends
name|UriChunk
block|{
specifier|private
name|String
name|value
decl_stmt|;
specifier|private
name|Literal
parameter_list|()
block|{
comment|// empty constructor
block|}
specifier|public
specifier|static
name|Literal
name|create
parameter_list|(
name|String
name|uriChunk
parameter_list|)
block|{
if|if
condition|(
name|uriChunk
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|uriChunk
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"uriChunk is empty"
argument_list|)
throw|;
block|}
name|Literal
name|literal
init|=
operator|new
name|Literal
argument_list|()
decl_stmt|;
name|literal
operator|.
name|value
operator|=
name|uriChunk
expr_stmt|;
return|return
name|literal
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
block|}
comment|/**      * Variable of URITemplate. Variable has either "{varname:pattern}" syntax or "{varname}".      */
specifier|private
specifier|static
specifier|final
class|class
name|Variable
extends|extends
name|UriChunk
block|{
specifier|private
specifier|static
specifier|final
name|Pattern
name|VARIABLE_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"(\\w[-\\w\\.]*[ ]*)(\\:(.+))?"
argument_list|)
decl_stmt|;
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|Pattern
name|pattern
decl_stmt|;
specifier|private
name|Variable
parameter_list|()
block|{
comment|// empty constructor
block|}
comment|/**          * Creates variable from stringified part of URI.          *          * @param uriChunk uriChunk chunk that depicts variable          * @return Variable if variable was successfully created; null if uriChunk was not a variable          */
specifier|public
specifier|static
name|Variable
name|create
parameter_list|(
name|String
name|uriChunk
parameter_list|)
block|{
name|Variable
name|newVariable
init|=
operator|new
name|Variable
argument_list|()
decl_stmt|;
if|if
condition|(
name|uriChunk
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|uriChunk
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|CurlyBraceTokenizer
operator|.
name|insideBraces
argument_list|(
name|uriChunk
argument_list|)
condition|)
block|{
name|uriChunk
operator|=
name|CurlyBraceTokenizer
operator|.
name|stripBraces
argument_list|(
name|uriChunk
argument_list|)
operator|.
name|trim
argument_list|()
expr_stmt|;
name|Matcher
name|matcher
init|=
name|VARIABLE_PATTERN
operator|.
name|matcher
argument_list|(
name|uriChunk
argument_list|)
decl_stmt|;
if|if
condition|(
name|matcher
operator|.
name|matches
argument_list|()
condition|)
block|{
name|newVariable
operator|.
name|name
operator|=
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|matcher
operator|.
name|group
argument_list|(
literal|2
argument_list|)
operator|!=
literal|null
operator|&&
name|matcher
operator|.
name|group
argument_list|(
literal|3
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|String
name|patternExpression
init|=
name|matcher
operator|.
name|group
argument_list|(
literal|3
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
name|newVariable
operator|.
name|pattern
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|patternExpression
argument_list|)
expr_stmt|;
block|}
return|return
name|newVariable
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|String
name|getPattern
parameter_list|()
block|{
return|return
name|pattern
operator|!=
literal|null
condition|?
name|pattern
operator|.
name|pattern
argument_list|()
else|:
literal|null
return|;
block|}
comment|/**          * Checks whether value matches variable. If variable has pattern its checked against, otherwise true          * is returned.          *           * @param value value of variable          * @return true if value is valid for variable, false otherwise.          */
specifier|public
name|boolean
name|matches
parameter_list|(
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|pattern
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
else|else
block|{
return|return
name|pattern
operator|.
name|matcher
argument_list|(
name|value
argument_list|)
operator|.
name|matches
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|getValue
parameter_list|()
block|{
if|if
condition|(
name|pattern
operator|!=
literal|null
condition|)
block|{
return|return
literal|"{"
operator|+
name|name
operator|+
literal|":"
operator|+
name|pattern
operator|+
literal|"}"
return|;
block|}
else|else
block|{
return|return
literal|"{"
operator|+
name|name
operator|+
literal|"}"
return|;
block|}
block|}
block|}
comment|/**      * Splits string into parts inside and outside curly braces. Nested curly braces are ignored and treated      * as part inside top-level curly braces. Example: string "foo{bar{baz}}blah" is split into three tokens,      * "foo","{bar{baz}}" and "blah". When closed bracket is missing, whole unclosed part is returned as one      * token, e.g.: "foo{bar" is split into "foo" and "{bar". When opening bracket is missing, closing      * bracket is ignored and taken as part of current token e.g.: "foo{bar}baz}blah" is split into "foo",      * "{bar}" and "baz}blah".      *<p>      * This is helper class for {@link URITemplate} that enables recurring literals appearing next to regular      * expressions e.g. "/foo/{zipcode:[0-9]{5}}/". Nested expressions with closed sections, like open-closed      * brackets causes expression to be out of regular grammar (is context-free grammar) which are not      * supported by Java regexp version.      *       * @author amichalec      * @version $Rev$      */
specifier|static
class|class
name|CurlyBraceTokenizer
block|{
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|tokens
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|int
name|tokenIdx
decl_stmt|;
specifier|public
name|CurlyBraceTokenizer
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|boolean
name|outside
init|=
literal|true
decl_stmt|;
name|int
name|level
init|=
literal|0
decl_stmt|;
name|int
name|lastIdx
init|=
literal|0
decl_stmt|;
name|int
name|idx
decl_stmt|;
for|for
control|(
name|idx
operator|=
literal|0
init|;
name|idx
operator|<
name|string
operator|.
name|length
argument_list|()
condition|;
name|idx
operator|++
control|)
block|{
if|if
condition|(
name|string
operator|.
name|charAt
argument_list|(
name|idx
argument_list|)
operator|==
literal|'{'
condition|)
block|{
if|if
condition|(
name|outside
condition|)
block|{
if|if
condition|(
name|lastIdx
operator|<
name|idx
condition|)
block|{
name|tokens
operator|.
name|add
argument_list|(
name|string
operator|.
name|substring
argument_list|(
name|lastIdx
argument_list|,
name|idx
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|lastIdx
operator|=
name|idx
expr_stmt|;
name|outside
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|level
operator|++
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|string
operator|.
name|charAt
argument_list|(
name|idx
argument_list|)
operator|==
literal|'}'
operator|&&
operator|!
name|outside
condition|)
block|{
if|if
condition|(
name|level
operator|>
literal|0
condition|)
block|{
name|level
operator|--
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|lastIdx
operator|<
name|idx
condition|)
block|{
name|tokens
operator|.
name|add
argument_list|(
name|string
operator|.
name|substring
argument_list|(
name|lastIdx
argument_list|,
name|idx
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|lastIdx
operator|=
name|idx
operator|+
literal|1
expr_stmt|;
name|outside
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|lastIdx
operator|<
name|idx
condition|)
block|{
name|tokens
operator|.
name|add
argument_list|(
name|string
operator|.
name|substring
argument_list|(
name|lastIdx
argument_list|,
name|idx
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**          * Token is enclosed by curly braces.          *           * @param token          *            text to verify          * @return true if enclosed, false otherwise.          */
specifier|public
specifier|static
name|boolean
name|insideBraces
parameter_list|(
name|String
name|token
parameter_list|)
block|{
return|return
name|token
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
operator|==
literal|'{'
operator|&&
name|token
operator|.
name|charAt
argument_list|(
name|token
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
operator|==
literal|'}'
return|;
block|}
comment|/**          * Strips token from enclosed curly braces. If token is not enclosed method          * has no side effect.          *           * @param token          *            text to verify          * @return text stripped from curly brace begin-end pair.          */
specifier|public
specifier|static
name|String
name|stripBraces
parameter_list|(
name|String
name|token
parameter_list|)
block|{
if|if
condition|(
name|insideBraces
argument_list|(
name|token
argument_list|)
condition|)
block|{
return|return
name|token
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|token
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|token
return|;
block|}
block|}
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|tokens
operator|.
name|size
argument_list|()
operator|>
name|tokenIdx
return|;
block|}
specifier|public
name|String
name|next
parameter_list|()
block|{
if|if
condition|(
name|hasNext
argument_list|()
condition|)
block|{
return|return
name|tokens
operator|.
name|get
argument_list|(
name|tokenIdx
operator|++
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"no more elements"
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

