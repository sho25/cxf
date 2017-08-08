begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/*  * This software consists of voluntary contributions made by many  * individuals on behalf of the Apache Software Foundation.  For more  * information on the Apache Software Foundation, please see  *<http://www.apache.org/>.  *  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|https
operator|.
name|httpclient
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|IDN
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|Locale
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
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_comment
comment|/**  * Utility class that can test if DNS names match the content of the Public Suffix List.  *<p>  * An up-to-date list of suffixes can be obtained from  *<a href="http://publicsuffix.org/">publicsuffix.org</a>  *  * Copied from httpclient.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PublicSuffixMatcher
block|{
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|DomainType
argument_list|>
name|rules
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|DomainType
argument_list|>
name|exceptions
decl_stmt|;
specifier|public
name|PublicSuffixMatcher
parameter_list|(
specifier|final
name|Collection
argument_list|<
name|String
argument_list|>
name|rules
parameter_list|,
specifier|final
name|Collection
argument_list|<
name|String
argument_list|>
name|exceptions
parameter_list|)
block|{
name|this
argument_list|(
name|DomainType
operator|.
name|UNKNOWN
argument_list|,
name|rules
argument_list|,
name|exceptions
argument_list|)
expr_stmt|;
block|}
specifier|public
name|PublicSuffixMatcher
parameter_list|(
specifier|final
name|DomainType
name|domainType
parameter_list|,
specifier|final
name|Collection
argument_list|<
name|String
argument_list|>
name|rules
parameter_list|,
specifier|final
name|Collection
argument_list|<
name|String
argument_list|>
name|exceptions
parameter_list|)
block|{
if|if
condition|(
name|domainType
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Domain type is null"
argument_list|)
throw|;
block|}
if|if
condition|(
name|rules
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Domain suffix rules are null"
argument_list|)
throw|;
block|}
name|this
operator|.
name|rules
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|DomainType
argument_list|>
argument_list|(
name|rules
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|rule
range|:
name|rules
control|)
block|{
name|this
operator|.
name|rules
operator|.
name|put
argument_list|(
name|rule
argument_list|,
name|domainType
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|exceptions
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|DomainType
argument_list|>
argument_list|()
expr_stmt|;
if|if
condition|(
name|exceptions
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|exception
range|:
name|exceptions
control|)
block|{
name|this
operator|.
name|exceptions
operator|.
name|put
argument_list|(
name|exception
argument_list|,
name|domainType
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|PublicSuffixMatcher
parameter_list|(
specifier|final
name|Collection
argument_list|<
name|PublicSuffixList
argument_list|>
name|lists
parameter_list|)
block|{
if|if
condition|(
name|lists
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Domain suffix lists are null"
argument_list|)
throw|;
block|}
name|this
operator|.
name|rules
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|DomainType
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|exceptions
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|DomainType
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|PublicSuffixList
name|list
range|:
name|lists
control|)
block|{
specifier|final
name|DomainType
name|domainType
init|=
name|list
operator|.
name|getType
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|rule
range|:
name|list
operator|.
name|getRules
argument_list|()
control|)
block|{
name|this
operator|.
name|rules
operator|.
name|put
argument_list|(
name|rule
argument_list|,
name|domainType
argument_list|)
expr_stmt|;
block|}
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|listExceptions
init|=
name|list
operator|.
name|getExceptions
argument_list|()
decl_stmt|;
if|if
condition|(
name|listExceptions
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|exception
range|:
name|listExceptions
control|)
block|{
name|this
operator|.
name|exceptions
operator|.
name|put
argument_list|(
name|exception
argument_list|,
name|domainType
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
specifier|static
name|boolean
name|hasEntry
parameter_list|(
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|DomainType
argument_list|>
name|map
parameter_list|,
specifier|final
name|String
name|rule
parameter_list|,
specifier|final
name|DomainType
name|expectedType
parameter_list|)
block|{
if|if
condition|(
name|map
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
specifier|final
name|DomainType
name|domainType
init|=
name|map
operator|.
name|get
argument_list|(
name|rule
argument_list|)
decl_stmt|;
if|if
condition|(
name|domainType
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|expectedType
operator|==
literal|null
operator|||
name|domainType
operator|.
name|equals
argument_list|(
name|expectedType
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|hasRule
parameter_list|(
specifier|final
name|String
name|rule
parameter_list|,
specifier|final
name|DomainType
name|expectedType
parameter_list|)
block|{
return|return
name|hasEntry
argument_list|(
name|this
operator|.
name|rules
argument_list|,
name|rule
argument_list|,
name|expectedType
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|hasException
parameter_list|(
specifier|final
name|String
name|exception
parameter_list|,
specifier|final
name|DomainType
name|expectedType
parameter_list|)
block|{
return|return
name|hasEntry
argument_list|(
name|this
operator|.
name|exceptions
argument_list|,
name|exception
argument_list|,
name|expectedType
argument_list|)
return|;
block|}
comment|/**      * Returns registrable part of the domain for the given domain name or {@code null}      * if given domain represents a public suffix.      *      * @param domain      * @return domain root      */
specifier|public
name|String
name|getDomainRoot
parameter_list|(
specifier|final
name|String
name|domain
parameter_list|)
block|{
return|return
name|getDomainRoot
argument_list|(
name|domain
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Returns registrable part of the domain for the given domain name or {@code null}      * if given domain represents a public suffix.      *      * @param domain      * @param expectedType expected domain type or {@code null} if any.      * @return domain root      */
specifier|public
name|String
name|getDomainRoot
parameter_list|(
specifier|final
name|String
name|domain
parameter_list|,
specifier|final
name|DomainType
name|expectedType
parameter_list|)
block|{
if|if
condition|(
name|domain
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|domain
operator|.
name|startsWith
argument_list|(
literal|"."
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|domainName
init|=
literal|null
decl_stmt|;
name|String
name|segment
init|=
name|domain
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|ROOT
argument_list|)
decl_stmt|;
while|while
condition|(
name|segment
operator|!=
literal|null
condition|)
block|{
comment|// An exception rule takes priority over any other matching rule.
if|if
condition|(
name|hasException
argument_list|(
name|IDN
operator|.
name|toUnicode
argument_list|(
name|segment
argument_list|)
argument_list|,
name|expectedType
argument_list|)
condition|)
block|{
return|return
name|segment
return|;
block|}
if|if
condition|(
name|hasRule
argument_list|(
name|IDN
operator|.
name|toUnicode
argument_list|(
name|segment
argument_list|)
argument_list|,
name|expectedType
argument_list|)
condition|)
block|{
break|break;
block|}
specifier|final
name|int
name|nextdot
init|=
name|segment
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
specifier|final
name|String
name|nextSegment
init|=
name|nextdot
operator|!=
operator|-
literal|1
condition|?
name|segment
operator|.
name|substring
argument_list|(
name|nextdot
operator|+
literal|1
argument_list|)
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|nextSegment
operator|!=
literal|null
operator|&&
name|hasRule
argument_list|(
literal|"*."
operator|+
name|IDN
operator|.
name|toUnicode
argument_list|(
name|nextSegment
argument_list|)
argument_list|,
name|expectedType
argument_list|)
condition|)
block|{
break|break;
block|}
if|if
condition|(
name|nextdot
operator|!=
operator|-
literal|1
condition|)
block|{
name|domainName
operator|=
name|segment
expr_stmt|;
block|}
name|segment
operator|=
name|nextSegment
expr_stmt|;
block|}
return|return
name|domainName
return|;
block|}
comment|/**      * Tests whether the given domain matches any of entry from the public suffix list.      */
specifier|public
name|boolean
name|matches
parameter_list|(
specifier|final
name|String
name|domain
parameter_list|)
block|{
return|return
name|matches
argument_list|(
name|domain
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Tests whether the given domain matches any of entry from the public suffix list.      *      * @param domain      * @param expectedType expected domain type or {@code null} if any.      * @return {@code true} if the given domain matches any of the public suffixes.      */
specifier|public
name|boolean
name|matches
parameter_list|(
specifier|final
name|String
name|domain
parameter_list|,
specifier|final
name|DomainType
name|expectedType
parameter_list|)
block|{
if|if
condition|(
name|domain
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
specifier|final
name|String
name|domainRoot
init|=
name|getDomainRoot
argument_list|(
name|domain
operator|.
name|startsWith
argument_list|(
literal|"."
argument_list|)
condition|?
name|domain
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
else|:
name|domain
argument_list|,
name|expectedType
argument_list|)
decl_stmt|;
return|return
name|domainRoot
operator|==
literal|null
return|;
block|}
block|}
end_class

end_unit

