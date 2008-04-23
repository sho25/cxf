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
name|javascript
package|;
end_package

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
name|endpoint
operator|.
name|Server
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
name|feature
operator|.
name|AbstractFeature
import|;
end_import

begin_comment
comment|/**  * This class provides configuration options to the JavaScript client generator.  * Attach this feature to control namespace mapping and other options.   *<pre>  *<![CDATA[<jaxws:endpoint ...><jaxws:features><bean class="org.apache.cxf.javascript.JavascriptOptionsFeature"></bean></jaxws:features></jaxws:endpoint>   ]]></pre>   * At this time, there is no corresponding WSDL extension for this information.  */
end_comment

begin_class
specifier|public
class|class
name|JavascriptOptionsFeature
extends|extends
name|AbstractFeature
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespacePrefixMap
decl_stmt|;
comment|/**      * Retrieve the map from namespace URI strings to JavaScript function prefixes.      * @return the map      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getNamespacePrefixMap
parameter_list|()
block|{
return|return
name|namespacePrefixMap
return|;
block|}
comment|/**      * Set the map from namespace URI strings to Javascript function prefixes.      * @param namespacePrefixMap the map from namespace URI strings to JavaScript function prefixes.      */
specifier|public
name|void
name|setNamespacePrefixMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespacePrefixMap
parameter_list|)
block|{
name|this
operator|.
name|namespacePrefixMap
operator|=
name|namespacePrefixMap
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|initialize
parameter_list|(
name|Server
name|server
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
comment|//  server.getEndpoint().getActiveFeatures().add(this);
block|}
block|}
end_class

end_unit

