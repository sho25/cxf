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
name|phase
package|;
end_package

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
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
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
name|SortedArraySet
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
name|message
operator|.
name|MessageUtils
import|;
end_import

begin_comment
comment|/**  * Provides a starting point implementation for a interceptors that  * participate in phased message processing. Developers should extend from  * this class when implementing custom interceptors.  * Developers need to provide an implementation for handleMessage() and  * can override the handleFault() implementation. They should not override  * the other methods.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractPhaseInterceptor
parameter_list|<
name|T
extends|extends
name|Message
parameter_list|>
implements|implements
name|PhaseInterceptor
argument_list|<
name|T
argument_list|>
block|{
specifier|private
specifier|final
name|String
name|id
decl_stmt|;
specifier|private
specifier|final
name|String
name|phase
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|before
init|=
operator|new
name|SortedArraySet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|after
init|=
operator|new
name|SortedArraySet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Instantiates the interceptor to live in a specified phase. The      * interceptor's id will be set to the name of the implementing class.      *      * @param phase the interceptor's phase      */
specifier|public
name|AbstractPhaseInterceptor
parameter_list|(
name|String
name|phase
parameter_list|)
block|{
name|this
argument_list|(
literal|null
argument_list|,
name|phase
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * Instantiates the interceptor with a specified id.      *      * @param i the interceptor's id      * @param p the interceptor's phase      */
specifier|public
name|AbstractPhaseInterceptor
parameter_list|(
name|String
name|i
parameter_list|,
name|String
name|p
parameter_list|)
block|{
name|this
argument_list|(
name|i
argument_list|,
name|p
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * Instantiates the interceptor and specifies if it gets a system      * determined unique id. If<code>uniqueId</code> is set to true the      * interceptor's id will be determined by the runtime. If      *<code>uniqueId</code> is set to false, the implementing class' name      * is used as the id.      *      * @param phase the interceptor's phase      * @param uniqueId true to have a unique ID generated      */
specifier|public
name|AbstractPhaseInterceptor
parameter_list|(
name|String
name|phase
parameter_list|,
name|boolean
name|uniqueId
parameter_list|)
block|{
name|this
argument_list|(
literal|null
argument_list|,
name|phase
argument_list|,
name|uniqueId
argument_list|)
expr_stmt|;
block|}
comment|/**      * Instantiates the interceptor with a specified id or with a system      * determined unique id. The specified id will be used unless      *<code>uniqueId</code> is set to true.      *      * @param i the interceptor's id      * @param p the interceptor's phase      * @param uniqueId      */
specifier|public
name|AbstractPhaseInterceptor
parameter_list|(
name|String
name|i
parameter_list|,
name|String
name|p
parameter_list|,
name|boolean
name|uniqueId
parameter_list|)
block|{
if|if
condition|(
name|i
operator|==
literal|null
condition|)
block|{
name|i
operator|=
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|uniqueId
condition|)
block|{
name|i
operator|+=
name|System
operator|.
name|identityHashCode
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
name|id
operator|=
name|i
expr_stmt|;
name|phase
operator|=
name|p
expr_stmt|;
block|}
comment|/**      * Specifies that the current interceptor needs to be added to the      * interceptor chain before the specified collection of interceptors.      * This method replaces any existing list with the provided list.      *      * @param i a collection of interceptor ids      */
specifier|public
name|void
name|setBefore
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|i
parameter_list|)
block|{
name|before
operator|.
name|clear
argument_list|()
expr_stmt|;
name|before
operator|.
name|addAll
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
comment|/**      * Specifies that the current interceptor needs to be added to the      * interceptor chain after the specified collection of interceptors.      * This method replaces any existing list with the provided list.      *      * @param i a collection of interceptor ids      */
specifier|public
name|void
name|setAfter
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|i
parameter_list|)
block|{
name|after
operator|.
name|clear
argument_list|()
expr_stmt|;
name|after
operator|.
name|addAll
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
comment|/**      * Specifies that the current interceptor needs to be added to the      * interceptor chain before the specified collection of interceptors.      *      * @param i a collection of interceptor ids      */
specifier|public
name|void
name|addBefore
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|i
parameter_list|)
block|{
name|before
operator|.
name|addAll
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
comment|/**      * Specifies that the current interceptor needs to be added to the      * interceptor chain after the specified collection of interceptors.      *      * @param i a collection of interceptor ids      */
specifier|public
name|void
name|addAfter
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|i
parameter_list|)
block|{
name|after
operator|.
name|addAll
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
comment|/**      * Specifies that the current interceptor needs to be added to the      * interceptor chain before the specified interceptor.      *      * @param i an interceptor id      */
specifier|public
name|void
name|addBefore
parameter_list|(
name|String
name|i
parameter_list|)
block|{
name|before
operator|.
name|add
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
comment|/**      * Specifies that the current interceptor needs to be added to the      * interceptor chain after the specified interceptor.      *      * @param i an interceptor id      */
specifier|public
name|void
name|addAfter
parameter_list|(
name|String
name|i
parameter_list|)
block|{
name|after
operator|.
name|add
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|getAfter
parameter_list|()
block|{
return|return
name|after
return|;
block|}
specifier|public
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|getBefore
parameter_list|()
block|{
return|return
name|before
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|PhaseInterceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|getAdditionalInterceptors
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
specifier|final
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
specifier|public
specifier|final
name|String
name|getPhase
parameter_list|()
block|{
return|return
name|phase
return|;
block|}
specifier|public
name|void
name|handleFault
parameter_list|(
name|T
name|message
parameter_list|)
block|{     }
specifier|public
name|boolean
name|isGET
parameter_list|(
name|T
name|message
parameter_list|)
block|{
name|String
name|method
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|)
decl_stmt|;
return|return
literal|"GET"
operator|.
name|equals
argument_list|(
name|method
argument_list|)
operator|&&
name|message
operator|.
name|getContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
operator|==
literal|null
return|;
block|}
comment|/**      * Determine if current messaging role is that of requestor.      *      * @param message the current Message      * @return true if the current messaging role is that of requestor      */
specifier|protected
name|boolean
name|isRequestor
parameter_list|(
name|T
name|message
parameter_list|)
block|{
return|return
name|MessageUtils
operator|.
name|isRequestor
argument_list|(
name|message
argument_list|)
return|;
block|}
block|}
end_class

end_unit

