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
name|tracing
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
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
name|phase
operator|.
name|PhaseInterceptorChain
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tracing
operator|.
name|TracerHeaders
operator|.
name|DEFAULT_HEADER_SPAN_ID
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractTracingProvider
block|{
specifier|protected
specifier|static
name|String
name|getSpanIdHeader
parameter_list|()
block|{
return|return
name|getHeaderOrDefault
argument_list|(
name|TracerHeaders
operator|.
name|HEADER_SPAN_ID
argument_list|,
name|DEFAULT_HEADER_SPAN_ID
argument_list|)
return|;
block|}
specifier|protected
specifier|static
class|class
name|TraceScopeHolder
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Serializable
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|5985783659818936359L
decl_stmt|;
specifier|private
specifier|final
name|T
name|scope
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|detached
decl_stmt|;
specifier|public
name|TraceScopeHolder
parameter_list|(
specifier|final
name|T
name|scope
parameter_list|,
specifier|final
name|boolean
name|detached
parameter_list|)
block|{
name|this
operator|.
name|scope
operator|=
name|scope
expr_stmt|;
name|this
operator|.
name|detached
operator|=
name|detached
expr_stmt|;
block|}
specifier|public
name|T
name|getScope
parameter_list|()
block|{
return|return
name|scope
return|;
block|}
specifier|public
name|boolean
name|isDetached
parameter_list|()
block|{
return|return
name|detached
return|;
block|}
block|}
specifier|private
specifier|static
name|String
name|getHeaderOrDefault
parameter_list|(
specifier|final
name|String
name|property
parameter_list|,
specifier|final
name|String
name|fallback
parameter_list|)
block|{
specifier|final
name|Message
name|message
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
specifier|final
name|Object
name|header
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|property
argument_list|)
decl_stmt|;
if|if
condition|(
name|header
operator|instanceof
name|String
condition|)
block|{
specifier|final
name|String
name|name
init|=
operator|(
name|String
operator|)
name|header
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
name|name
return|;
block|}
block|}
block|}
return|return
name|fallback
return|;
block|}
specifier|protected
name|String
name|buildSpanDescription
parameter_list|(
specifier|final
name|String
name|path
parameter_list|,
specifier|final
name|String
name|method
parameter_list|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|method
argument_list|)
condition|)
block|{
return|return
name|path
return|;
block|}
return|return
name|method
operator|+
literal|" "
operator|+
name|path
return|;
block|}
block|}
end_class

end_unit

