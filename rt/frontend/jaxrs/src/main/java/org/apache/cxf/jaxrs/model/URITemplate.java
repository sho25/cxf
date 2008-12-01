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
name|List
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
name|UNLIMITED_REGEX_SUFFIX
init|=
literal|"(/)?"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FINAL_MATCH_GROUP
init|=
literal|"FINAL_MATCH_GROUP"
decl_stmt|;
comment|/**      * The regular expression for matching URI templates and names.      */
specifier|private
specifier|static
specifier|final
name|Pattern
name|TEMPLATE_NAMES_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\\{(\\w[-\\w\\.]*)\\}"
argument_list|)
decl_stmt|;
comment|/**      * A URI template is converted into a regular expression by substituting      * (.*?) for each occurrence of {\([w- 14 \. ]+?\)} within the URL      * template      */
specifier|private
specifier|static
specifier|final
name|String
name|PATH_VARIABLE_REGEX
init|=
literal|"([^/]+?)"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PATH_UNLIMITED_VARIABLE_REGEX
init|=
literal|"(.*?)"
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
name|templateVariables
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
specifier|public
name|URITemplate
parameter_list|(
name|String
name|theTemplate
parameter_list|)
block|{
name|this
argument_list|(
name|theTemplate
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|URITemplate
parameter_list|(
name|String
name|theTemplate
parameter_list|,
name|boolean
name|limited
parameter_list|)
block|{
name|this
operator|.
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
name|stringBuilder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|// compute a regular expression from URI template
name|Matcher
name|matcher
init|=
name|TEMPLATE_NAMES_PATTERN
operator|.
name|matcher
argument_list|(
name|template
argument_list|)
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|matcher
operator|.
name|find
argument_list|()
condition|)
block|{
name|literalChars
operator|.
name|append
argument_list|(
name|template
operator|.
name|substring
argument_list|(
name|i
argument_list|,
name|matcher
operator|.
name|start
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|copyURITemplateCharacters
argument_list|(
name|template
argument_list|,
name|i
argument_list|,
name|matcher
operator|.
name|start
argument_list|()
argument_list|,
name|stringBuilder
argument_list|)
expr_stmt|;
name|i
operator|=
name|matcher
operator|.
name|end
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|limited
operator|&&
name|i
operator|==
name|template
operator|.
name|length
argument_list|()
condition|)
block|{
name|stringBuilder
operator|.
name|append
argument_list|(
name|PATH_UNLIMITED_VARIABLE_REGEX
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|stringBuilder
operator|.
name|append
argument_list|(
name|PATH_VARIABLE_REGEX
argument_list|)
expr_stmt|;
block|}
name|names
operator|.
name|add
argument_list|(
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|literalChars
operator|.
name|append
argument_list|(
name|template
operator|.
name|substring
argument_list|(
name|i
argument_list|,
name|template
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|copyURITemplateCharacters
argument_list|(
name|template
argument_list|,
name|i
argument_list|,
name|template
operator|.
name|length
argument_list|()
argument_list|,
name|stringBuilder
argument_list|)
expr_stmt|;
name|literals
operator|=
name|literalChars
operator|.
name|toString
argument_list|()
expr_stmt|;
name|templateVariables
operator|=
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|names
argument_list|)
expr_stmt|;
name|int
name|endPos
init|=
name|stringBuilder
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
name|stringBuilder
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
name|stringBuilder
operator|.
name|deleteCharAt
argument_list|(
name|endPos
argument_list|)
expr_stmt|;
block|}
name|stringBuilder
operator|.
name|append
argument_list|(
name|limited
condition|?
name|LIMITED_REGEX_SUFFIX
else|:
name|UNLIMITED_REGEX_SUFFIX
argument_list|)
expr_stmt|;
name|templateRegexPattern
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|stringBuilder
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
specifier|public
name|int
name|getNumberOfGroups
parameter_list|()
block|{
return|return
name|templateVariables
operator|.
name|size
argument_list|()
return|;
block|}
specifier|private
name|void
name|copyURITemplateCharacters
parameter_list|(
name|String
name|templateValue
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|end
parameter_list|,
name|StringBuilder
name|b
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
name|start
init|;
name|i
operator|<
name|end
condition|;
name|i
operator|++
control|)
block|{
name|char
name|c
init|=
name|templateValue
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|'?'
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|"\\?"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|b
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
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
comment|// we might be trying to match one or few path segments containing matrix
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
operator|!=
operator|-
literal|1
condition|)
block|{
comment|// if it's URI template variable then keep the original value
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
else|else
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
name|i
init|=
literal|1
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|templateVariables
control|)
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
name|templateVariableToValue
operator|.
name|add
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|// The right hand side value, might be used to further resolve sub-resources.
name|String
name|finalGroup
init|=
name|m
operator|.
name|group
argument_list|(
name|i
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
specifier|public
specifier|static
name|URITemplate
name|createTemplate
parameter_list|(
name|ClassResourceInfo
name|cri
parameter_list|,
name|Path
name|path
parameter_list|)
block|{
if|if
condition|(
name|path
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
name|String
name|pathValue
init|=
name|path
operator|.
name|value
argument_list|()
decl_stmt|;
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
block|}
end_class

end_unit

