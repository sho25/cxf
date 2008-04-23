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
name|xmlbeans
operator|.
name|rpc
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebMethod
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebParam
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebService
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|soap
operator|.
name|SOAPBinding
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|net
operator|.
name|webservicex
operator|.
name|WeatherData
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
name|SOAPConstants
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
name|wsdl
operator|.
name|WSDLConstants
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
name|xmlbeans
operator|.
name|AbstractXmlBeansTest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * @author<a href="mailto:dan@envoisolutions.com">Dan Diephouse</a>  */
end_comment

begin_class
specifier|public
class|class
name|WeatherServiceRPCLitTest
extends|extends
name|AbstractXmlBeansTest
block|{
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
comment|//xsf.setStyle(SoapConstants.STYLE_RPC);
name|createService
argument_list|(
name|RPCWeatherService
operator|.
name|class
argument_list|,
operator|new
name|RPCWeatherService
argument_list|()
argument_list|,
literal|"WeatherService"
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://www.webservicex.net"
argument_list|,
literal|"WeatherService"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInvoke
parameter_list|()
throws|throws
name|Exception
block|{
name|Node
name|response
init|=
name|invoke
argument_list|(
literal|"WeatherService"
argument_list|,
literal|"SetWeatherData.xml"
argument_list|)
decl_stmt|;
name|addNamespace
argument_list|(
literal|"w"
argument_list|,
literal|"http://www.webservicex.net"
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//w:setWeatherDataResponse"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|response
operator|=
name|invoke
argument_list|(
literal|"WeatherService"
argument_list|,
literal|"GetWeatherData.xml"
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//w:getWeatherDataResponse/return"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//w:getWeatherDataResponse/return/w:MaxTemperatureC[text()='1']"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//w:getWeatherDataResponse/return/w:MaxTemperatureF[text()='1']"
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWSDL
parameter_list|()
throws|throws
name|Exception
block|{
name|Node
name|wsdl
init|=
name|getWSDLDocument
argument_list|(
literal|"WeatherService"
argument_list|)
decl_stmt|;
name|addNamespace
argument_list|(
literal|"w"
argument_list|,
name|WSDLConstants
operator|.
name|NS_WSDL11
argument_list|)
expr_stmt|;
name|addNamespace
argument_list|(
literal|"xsd"
argument_list|,
name|SOAPConstants
operator|.
name|XSD
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//w:message[@name='getWeatherDataResponse']/w:part[@type='tns:WeatherData']"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SOAPBinding
argument_list|(
name|style
operator|=
name|SOAPBinding
operator|.
name|Style
operator|.
name|RPC
argument_list|)
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://www.webservicex.net"
argument_list|)
specifier|public
specifier|static
class|class
name|RPCWeatherService
block|{
annotation|@
name|WebMethod
specifier|public
name|WeatherData
name|getWeatherData
parameter_list|()
block|{
name|WeatherData
name|data
init|=
name|WeatherData
operator|.
name|Factory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|data
operator|.
name|setMaxTemperatureC
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
name|data
operator|.
name|setMaxTemperatureF
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
return|return
name|data
return|;
block|}
annotation|@
name|WebMethod
specifier|public
name|void
name|setWeatherData
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"data"
argument_list|)
name|WeatherData
name|data
parameter_list|)
block|{          }
block|}
block|}
end_class

end_unit

