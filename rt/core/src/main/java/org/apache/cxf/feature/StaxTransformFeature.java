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
name|feature
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|Bus
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
name|injection
operator|.
name|NoJSR250Annotations
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
name|interceptor
operator|.
name|InterceptorProvider
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
name|interceptor
operator|.
name|transform
operator|.
name|TransformInInterceptor
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
name|interceptor
operator|.
name|transform
operator|.
name|TransformOutInterceptor
import|;
end_import

begin_comment
comment|/**  *<pre>  *<![CDATA[<jaxws:endpoint ...><jaxws:features><bean class="org.apache.cxf.feature.StaxTransformFeature"/></jaxws:features></jaxws:endpoint>   ]]></pre>  */
end_comment

begin_class
annotation|@
name|NoJSR250Annotations
specifier|public
class|class
name|StaxTransformFeature
extends|extends
name|AbstractFeature
block|{
specifier|private
name|TransformInInterceptor
name|in
init|=
operator|new
name|TransformInInterceptor
argument_list|()
decl_stmt|;
specifier|private
name|TransformOutInterceptor
name|out
init|=
operator|new
name|TransformOutInterceptor
argument_list|()
decl_stmt|;
specifier|public
name|StaxTransformFeature
parameter_list|()
block|{
comment|//
block|}
annotation|@
name|Override
specifier|protected
name|void
name|initializeProvider
parameter_list|(
name|InterceptorProvider
name|provider
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|provider
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|provider
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|provider
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setOutTransformElements
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|outElements
parameter_list|)
block|{
name|out
operator|.
name|setOutTransformElements
argument_list|(
name|outElements
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setAttributesToElements
parameter_list|(
name|boolean
name|value
parameter_list|)
block|{
name|out
operator|.
name|setAttributesToElements
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setOutAppendElements
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
parameter_list|)
block|{
name|out
operator|.
name|setOutAppendElements
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setOutDropElements
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|dropElementsSet
parameter_list|)
block|{
name|out
operator|.
name|setOutDropElements
argument_list|(
name|dropElementsSet
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setInAppendElements
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|inElements
parameter_list|)
block|{
name|in
operator|.
name|setInAppendElements
argument_list|(
name|inElements
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setInDropElements
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|dropElementsSet
parameter_list|)
block|{
name|in
operator|.
name|setInDropElements
argument_list|(
name|dropElementsSet
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setInTransformElements
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|inElements
parameter_list|)
block|{
name|in
operator|.
name|setInTransformElements
argument_list|(
name|inElements
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setContextPropertyName
parameter_list|(
name|String
name|propertyName
parameter_list|)
block|{
name|in
operator|.
name|setContextPropertyName
argument_list|(
name|propertyName
argument_list|)
expr_stmt|;
name|out
operator|.
name|setContextPropertyName
argument_list|(
name|propertyName
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

