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
name|transport
operator|.
name|jms
operator|.
name|uri
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLDecoder
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
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
name|common
operator|.
name|util
operator|.
name|StringUtils
import|;
end_import

begin_comment
comment|/**  * Unfortunately soap/jms URIs are not recognized correctly in URI.  * So this class is specialized on parsing jms uris into their parts  */
end_comment

begin_class
specifier|final
class|class
name|JMSURIParser
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|JMSURIParser
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|uri
decl_stmt|;
name|int
name|pos
decl_stmt|;
specifier|private
name|String
name|scheme
decl_stmt|;
specifier|private
name|String
name|variant
decl_stmt|;
specifier|private
name|String
name|destination
decl_stmt|;
specifier|private
name|String
name|query
decl_stmt|;
specifier|public
name|JMSURIParser
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
name|UnsafeUriCharactersEncoder
operator|.
name|encode
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|this
operator|.
name|scheme
operator|=
name|parseUntil
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|this
operator|.
name|variant
operator|=
name|parseUntil
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|this
operator|.
name|destination
operator|=
name|parseUntil
argument_list|(
literal|"?"
argument_list|)
expr_stmt|;
name|String
name|rest
init|=
name|parseToEnd
argument_list|()
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|destination
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|destination
operator|=
name|rest
expr_stmt|;
name|this
operator|.
name|query
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|query
operator|=
name|rest
expr_stmt|;
block|}
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"Creating endpoint uri=["
operator|+
name|uri
operator|+
literal|"], destination=["
operator|+
name|destination
operator|+
literal|"], query=["
operator|+
name|query
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|parseToEnd
parameter_list|()
block|{
return|return
name|uri
operator|.
name|substring
argument_list|(
name|pos
argument_list|,
name|uri
operator|.
name|length
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|String
name|parseUntil
parameter_list|(
name|String
name|separator
parameter_list|)
block|{
name|int
name|separatorPos
init|=
name|uri
operator|.
name|indexOf
argument_list|(
name|separator
argument_list|,
name|pos
argument_list|)
decl_stmt|;
if|if
condition|(
name|separatorPos
operator|!=
operator|-
literal|1
condition|)
block|{
name|String
name|found
init|=
name|uri
operator|.
name|substring
argument_list|(
name|pos
argument_list|,
name|separatorPos
argument_list|)
decl_stmt|;
name|pos
operator|=
name|separatorPos
operator|+
literal|1
expr_stmt|;
return|return
name|found
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parseQuery
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|rc
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|query
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|parameters
init|=
name|StringUtils
operator|.
name|split
argument_list|(
name|query
argument_list|,
literal|"&"
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|parameter
range|:
name|parameters
control|)
block|{
name|int
name|p
init|=
name|parameter
operator|.
name|indexOf
argument_list|(
literal|"="
argument_list|)
decl_stmt|;
if|if
condition|(
name|p
operator|>=
literal|0
condition|)
block|{
name|String
name|name
init|=
name|urldecode
argument_list|(
name|parameter
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|p
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|urldecode
argument_list|(
name|parameter
operator|.
name|substring
argument_list|(
name|p
operator|+
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|rc
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|rc
operator|.
name|put
argument_list|(
name|parameter
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|rc
return|;
block|}
specifier|private
specifier|static
name|String
name|urldecode
parameter_list|(
name|String
name|s
parameter_list|)
block|{
try|try
block|{
return|return
name|URLDecoder
operator|.
name|decode
argument_list|(
name|s
argument_list|,
literal|"UTF-8"
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Encoding UTF-8 not supported"
argument_list|)
throw|;
block|}
block|}
specifier|public
name|String
name|getScheme
parameter_list|()
block|{
return|return
name|scheme
return|;
block|}
specifier|public
name|String
name|getVariant
parameter_list|()
block|{
return|return
name|variant
return|;
block|}
specifier|public
name|String
name|getDestination
parameter_list|()
block|{
return|return
name|destination
return|;
block|}
block|}
end_class

end_unit

